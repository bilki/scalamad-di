package com.lambdarat.cake

import com.lambdarat.common.Domain.{Group, MeetupError, User}

trait ManagerComponent { self: NotifierComponent =>

  def manager: Manager

  trait Manager {

    def addUserToGroup(user: User, group: Group): Either[MeetupError, Group]

    def removeUserFromGroup(uid: User.Id, group: Group): Either[MeetupError, Group]

  }

}
