package com.lambdarat.reader

import com.lambdarat.common.Domain.{Group, MeetupError, User}

import scala.language.higherKinds

trait Manager[F[_]] {

  def addUserToGroup(user: User, group: Group): F[Either[MeetupError, Group]]

  def removeUserFromGroup(uid: User.Id, group: Group): F[Either[MeetupError, Group]]

}
