package com.lambdarat.reader

import com.lambdarat.common.Domain._
import com.lambdarat.common.services.{GroupsImpl, Notifier, UsersImpl}
import com.lambdarat.reader.MeetupImplTypes.MeetupContext
import org.scalatest.{FlatSpec, Matchers}

import cats.Id
import cats.data.{EitherT, Reader}

class MeetupReaderTest extends FlatSpec with Matchers {

  type MeetupErrorOrValue[A] = EitherT[Id, MeetupError, A]

  implicit class ReaderEitherT[A, B, C](r: Reader[A, Either[B, C]]) {
    def toEitherT = {
      type ErrorOrValue[V] = EitherT[Id, B, V]

      r.mapF[ErrorOrValue, C](EitherT.fromEither[Id].apply[B, C])
    }
  }

  // This notifier does not notify anybody
  private val mockNotifier = new Notifier {

    override def notifyManagerEvent(event: GroupEvent, group: Group): Either[NotifierError, Seq[User.Id]] = {
      println(s"Not notifying users of group ${group.gid}")

      val userIds = group.users.collect { case User(_, _, Some(id)) => id }

      val mockedResult: Either[NotifierError, Seq[User.Id]] = event match {
        case GroupEvent.UserAdded(uid)    => Right(uid +: userIds)
        case GroupEvent.UserRemoved(uid)  => Right(userIds.filter(_ != uid))
      }

      mockedResult
    }

  }

  private val manager = ManagerImpl
  private val users = new UsersImpl
  private val groups = new GroupsImpl
  private val meetup = MeetupImpl

  val context = MeetupContext(
    users.getUser,
    users.createUser,
    users.deleteUser,
    groups.getGroup,
    groups.saveGroup,
    groups.closeGroup,
    manager.addUserToGroup(_, _).run(mockNotifier.notifyManagerEvent),
    manager.removeUserFromGroup(_, _).run(mockNotifier.notifyManagerEvent)
  )

  "A meetup" should "add a user to a group if both exist" in {

    val joinAttempt = for {
      uid   <- meetup.registerUser(User.Name("Pepe"), User.Age(25)).toEitherT
      gid   <- meetup.registerGroup(Group.Name("ScalaMAD")).toEitherT
      group <- meetup.joinUserToGroup(uid, gid).toEitherT  // No user notified in real life
    } yield {

      group.gid shouldBe Some(gid)

      group.users should have size 1

    }

    joinAttempt.run(context) shouldBe a[Right[_, _]]

  }

}
