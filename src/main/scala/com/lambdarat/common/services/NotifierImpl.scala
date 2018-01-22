package com.lambdarat.common.services

import com.lambdarat.common.Domain.{Group, GroupEvent, NotifierError, User}

import cats.data.EitherT

class NotifierImpl extends Notifier {

  override def notifyManagerEvent(event: GroupEvent, group: Group): Either[NotifierError, Seq[User.Id]] = {

    def userWithoutId(gid: Group.Id) = NotifierError(s"One of the users of the group $gid has no uid")

    lazy val groupWithoutId = NotifierError(s"The group ${group.name} has no identifier")

    def eventMessage(name: User.Name, gid: Group.Id) = event match {
      case add: GroupEvent.UserAdded     => s"Hi $name, the user ${add.uid} just joined the group $gid"
      case left: GroupEvent.UserRemoved  => s"Hi $name, the user ${left.uid} just left the group $gid"
    }

    import cats.instances.list._

    val attemptNotifications = for {
      gid   <- EitherT.fromEither(group.gid.toRight(groupWithoutId))
      user  <- EitherT.liftF(group.users.toList)
      uid   <- EitherT.fromEither(user.uid.toRight(userWithoutId(gid)))
    } yield {
      println(s"Notifying user $uid with message ${eventMessage(user.name, gid)}")

      uid
    }

    import cats.instances.either._
    import cats.syntax.traverse._

    attemptNotifications.value.sequence.map(_.toSeq)

  }

}
