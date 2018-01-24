package com.lambdarat.reader

import com.lambdarat.common.Domain.{Group, MeetupError, User}

import scala.language.higherKinds

trait Meetup[F[_]] {

  def registerUser(name: User.Name, age: User.Age): F[Either[MeetupError, User.Id]]

  def deleteUser(uid: User.Id): F[Either[MeetupError, User]]

  def registerGroup(name: Group.Name): F[Either[MeetupError, Group.Id]]

  def closeGroup(gid: Group.Id): F[Either[MeetupError, Group]]

  def getGroupUsers(gid: Group.Id): F[Either[MeetupError, Seq[User]]]

  def joinUserToGroup(uid: User.Id, gid: Group.Id): F[Either[MeetupError, Group]]

  def removeUserFromGroup(uid: User.Id, gid: Group.Id): F[Either[MeetupError, Group]]

}
