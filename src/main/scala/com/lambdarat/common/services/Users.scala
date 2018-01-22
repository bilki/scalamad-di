package com.lambdarat.common.services

import com.lambdarat.common.Domain.{User, UsersError}

trait Users {

  def createUser(user: User): Either[UsersError, User.Id]

  def getUser(uid: User.Id): Either[UsersError, User]

  def deleteUser(uid: User.Id): Either[UsersError, User]

}
