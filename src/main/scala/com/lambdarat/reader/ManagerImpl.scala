package com.lambdarat.reader

import com.lambdarat.common.Domain._
import com.lambdarat.reader.FuncManagerImpl.NotifyGroup
import com.lambdarat.reader.ManagerImplTypes.ManagerResult

import cats.data.Reader

object ManagerImplTypes {

  type ManagerResult[S] = Reader[NotifyGroup, S]

}

object ManagerImpl extends Manager[ManagerResult] {

  override def addUserToGroup(user: User, group: Group) = Reader(FuncManagerImpl.addUserToGroup(_)(user, group))

  override def removeUserFromGroup(uid: User.Id, group: Group) = Reader(FuncManagerImpl.removeUserFromGroup(_)(uid, group))

}
