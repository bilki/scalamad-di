package com.lambdarat.naive

import com.lambdarat.common.Domain.{Group, MeetupError, User}

trait Manager {

  def addUserToGroup(user: User, group: Group): Either[MeetupError, Group]

  def removeUserFromGroup(uid: User.Id, group: Group): Either[MeetupError, Group]

}

object Manager {

  sealed trait Event {

    def uid: User.Id

  }

  case class UserRemoved(uid: User.Id) extends Event

  case class UserAdded(uid: User.Id) extends Event

}