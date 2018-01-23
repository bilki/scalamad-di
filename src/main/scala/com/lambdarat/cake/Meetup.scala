package com.lambdarat.cake

import com.lambdarat.common.Domain.{Group, MeetupError, User}

trait Meetup {

  def registerUser(name: User.Name, age: User.Age): Either[MeetupError, User.Id]

  def deleteUser(uid: User.Id): Either[MeetupError, User]

  def registerGroup(name: Group.Name): Either[MeetupError, Group.Id]

  def closeGroup(gid: Group.Id): Either[MeetupError, Group]

  def getGroupUsers(gid: Group.Id): Either[MeetupError, Seq[User]]

  def joinUserToGroup(uid: User.Id, gid: Group.Id): Either[MeetupError, Group]

  def removeUserFromGroup(uid: User.Id, gid: Group.Id): Either[MeetupError, Group]

}
