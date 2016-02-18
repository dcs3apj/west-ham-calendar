package models

import enumeratum.{Enum, EnumEntry}
import json.JsonCodecs
import upickle.Js
import scalaz._
import Scalaz._

/**
  * Type safe links object
  * Created by alex on 14/02/16.
  */
case class Links[R <: Rel](private val links: Map[R, String] = Map.empty[R, String], private val _self: Option[String] = None) {

  def withSelf(href: String) = Links(links, Some(href))

  def withLink(rel: R, href: String) = Links(links + (rel -> href), _self)

  def withLinks(moreLinks: Map[R, String]) = Links(links ++ moreLinks, _self)

  def self: Option[String] = _self

  def apply(rel: R): Option[String] = links.get(rel)

  def required(rel: R): String = apply(rel).get
}

trait Rel extends EnumEntry {
  val rel: String

  final override val entryName = rel

  final override def toString = rel
}

abstract class Rel_(val rel: String) extends Rel

trait RelEnum[R <: Rel] extends Enum[R] with (String => Option[R]) {

  def apply(rel: String): Option[R] = withNameOption(rel)
}

object Links extends JsonCodecs {

  def withSelf[R <: Rel](href: String) = Links[R]().withSelf(href)

  def withLink[R <: Rel](rel: R, href: String) = Links[R]().withLink(rel, href)

  def withLinks[R <: Rel](moreLinks: Map[R, String]) = Links[R]().withLinks(moreLinks)

  def linksToJson[R <: Rel](links: Links[R]): Js.Value = {
    val allLinks = links.links.toSeq.map(rh => rh._1.rel -> rh._2) ++ links.self.map("self" -> _)
    val jsonLinks = allLinks.map { case (rel, href) =>
      Js.Obj(Map("rel" -> rel, "href" -> href).mapValues(Js.Str).toSeq :_*)
    }
    Js.Arr(jsonLinks :_*)
  }

  private def jsonToLink(value: Js.Value): ValidationNel[String, (String, String)] = value.jsObj { fields =>
    (fields.mandatory("rel", "Cannot find a rel property for links")(_.jsStr) |@|
      fields.mandatory("href", "Cannot find a href property for links")(_.jsStr)).tupled
  }

  def jsonToLinks[R <: Rel](f: String => Option[R])(value: Js.Value): ValidationNel[String, Links[R]] = {
    val singleLinks: \/[NonEmptyList[String], Seq[(String, String)]] = value.jsArr(jsonToLink).disjunction
    singleLinks.map { links =>
      links.foldLeft(new Links[R]()) { (existingLinks, newLink) =>
        val (rel, href) = newLink
        if ("self" == rel) {
          existingLinks.withSelf(href)
        }
        else {
          existingLinks.withLinks(f(rel).map(r => (r, href)).toMap)
        }
      }
    }.validation
  }
}