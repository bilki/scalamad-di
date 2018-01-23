package com.lambdarat.cake

import com.lambdarat.common.Domain._
import com.lambdarat.common.services.Notifier

trait ManagerComponentImpl extends ManagerComponent { self: NotifierComponent =>

  override val manager: Manager = new ManagerImpl

  class ManagerImpl extends Manager {

    override def addUserToGroup(user: User, group: Group): Either[MeetupError, Group] = {

      lazy val alreadyPresentError = GroupsError(s"User with uid ${user.uid} is already present in group ${group.gid}")

      lazy val userWithoutIdError = GroupsError(s"User ${user.name} came without a proper uid")

      for {
        uid <- user.uid.toRight(userWithoutIdError)
        _ <- Either.cond(!group.users.exists(_.uid == user.uid), (), alreadyPresentError)
        _ <- notifier.notifyManagerEvent(GroupEvent.UserAdded(uid), group)
      } yield {
        group.copy(users = user +: group.users)
      }

    }

    override def removeUserFromGroup(uid: User.Id, group: Group): Either[MeetupError, Group] = {

      lazy val notPresentError = GroupsError(s"User with uid $uid is not present in group ${group.gid}")

      val userIndex = group.users.indexWhere(_.uid.exists(_ == uid))

      lazy val groupWithoutUser = group.copy(users = group.users.take(userIndex) ++ group.users.drop(userIndex + 1))

      for {
        group <- Either.cond(userIndex != -1, groupWithoutUser, notPresentError)
        _ <- notifier.notifyManagerEvent(GroupEvent.UserRemoved(uid), group)
      } yield {
        group
      }

    }
  }

}
