package pdf

import argonaut.Argonaut._
import argonaut._
import pdf.PriorityPointsConfiguration.ClientType
import uk.co.unclealex.hammers.calendar.model.PersistedClientType
import uk.co.unclealex.hammers.calendar.model.PersistedClientType._

/**
 * Created by alex on 09/02/15.
 */

case class PriorityPointsConfiguration(clients: List[Client], contactDetails: ContactDetails, creditCard: CreditCard)
case class Client(name: String, referenceNumber: Int, clientType: Option[ClientType])


case class ContactDetails(address: String, daytimeTelephoneNumber: String, mobilePhoneNumber: String, emailAddress: String)
case class CreditCard(number: String, expiry: CreditCardDate, securityCode: Int, nameOnCard: String)
case class CreditCardDate(month: Int, year: Int)

object PriorityPointsConfiguration {

  type ClientType = PersistedClientType

  // JSON codecs
  private val clientTypeMappings = Seq(OAP -> "oap", Junior -> "junior")
  implicit def clientTypeEncoder: EncodeJson[ClientType] =
    EncodeJson((ct: ClientType) => jString(clientTypeMappings.find(_._1 == ct).get._2))
  implicit def clientTypeDecoder: DecodeJson[ClientType] =
    DecodeJson { c =>
      c.as[String].flatMap { token =>
        val optClientType = clientTypeMappings.find(_._2 == token).map(_._1)
        optClientType.map(DecodeResult.ok(_)).getOrElse(DecodeResult.fail(s"Unknown client type ${token}", c.history))
      }
    }

  implicit def clientCodec = casecodec3(Client.apply, Client.unapply)("name", "referenceNumber", "clientType")
  implicit def contactDetailsCodec =
    casecodec4(ContactDetails.apply, ContactDetails.unapply)("address", "daytimeTelephoneNumber", "mobilePhoneNumber", "emailAddress")
  implicit def creditCardDateCodec = casecodec2(CreditCardDate.apply, CreditCardDate.unapply)("month", "year")
  implicit def creditCardCodec =
    casecodec4(CreditCard.apply, CreditCard.unapply)("number", "expiry", "securityCode", "nameOnCard")
  implicit def priorityPointsConfigurationCodec =
    casecodec3(PriorityPointsConfiguration.apply, PriorityPointsConfiguration.unapply)("clients", "contactDetails", "creditCard")
}