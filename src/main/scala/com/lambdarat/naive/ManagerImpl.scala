package com.lambdarat.naive

import com.lambdarat.common.Domain.{Group, GroupsError, MeetupError, User}

class ManagerImpl extends Manager {

  private val notifier: Notifier = new NotifierImpl

  def addUserToGroup(user: User, group: Group): Either[MeetupError, Group] = {

    lazy val alreadyPresentError = GroupsError(s"User with uid ${user.uid} is already present in group ${group.gid}")

    lazy val userWithoutIdError = GroupsError(s"User ${user.name} came without a proper uid")

    for {
      uid <- user.uid.toRight(userWithoutIdError)
      _   <- Either.cond(!group.users.exists(_.uid == user.uid), (), alreadyPresentError)
      _   <- notifier.notifyManagerEvent(Manager.UserAdded(uid), group)
    } yield {
      group.copy(users = user +: group.users)
    }

  }

  def removeUserFromGroup(uid: User.Id, group: Group): Either[MeetupError, Group] = {

    lazy val notPresentError = GroupsError(s"User with uid $uid is not present in group ${group.gid}")

    val userIndex = group.users.indexWhere(_.uid.exists(_ == uid))

    lazy val groupWithoutUser = group.copy(users = group.users.take(userIndex) ++ group.users.drop(userIndex + 1))

    for {
      group <- Either.cond(userIndex != -1, groupWithoutUser, notPresentError)
      _     <- notifier.notifyManagerEvent(Manager.UserRemoved(uid), group)
    } yield {
      group
    }

  }

}
