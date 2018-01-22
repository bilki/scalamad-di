package com.lambdarat.implicits

import com.lambdarat.common.Domain.{Group, MeetupError, User}
import com.lambdarat.common.services.{Groups, Notifier, Users}

trait Meetup {

  def registerUser(name: User.Name, age: User.Age)(implicit users: Users): Either[MeetupError, User.Id]

  def deleteUser(uid: User.Id)(implicit users: Users): Either[MeetupError, User]

  def registerGroup(name: Group.Name)(implicit groups: Groups): Either[MeetupError, Group.Id]

  def closeGroup(gid: Group.Id)(implicit groups: Groups): Either[MeetupError, Group]

  def getGroupUsers(gid: Group.Id)(implicit groups: Groups): Either[MeetupError, Seq[User]]

  def joinUserToGroup(uid: User.Id, gid: Group.Id)
    (implicit users: Users, groups: Groups, manager: Manager, notifier: Notifier): Either[MeetupError, Group]

  def removeUserFromGroup(uid: User.Id, gid: Group.Id)
    (implicit users: Users, groups: Groups, manager: Manager, notifier: Notifier): Either[MeetupError, Group]

}
