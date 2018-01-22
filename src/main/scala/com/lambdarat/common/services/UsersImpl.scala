package com.lambdarat.common.services

import java.util.concurrent.atomic.AtomicLong

import com.lambdarat.common.Domain
import com.lambdarat.common.Domain.{User, UsersError}

import scala.collection.concurrent.TrieMap

class UsersImpl extends Users {

  private val users = TrieMap.empty[User.Id, User]

  private val userIdGenerator = new AtomicLong(0)

  override def createUser(user: Domain.User): Either[Domain.UsersError, User.Id] = {

    Either.cond(
      !users.values.exists(_.name == user.name),
      {
        val userId = User.Id(userIdGenerator.addAndGet(1))
        users += (userId -> user.copy(uid = Some(userId)))
        userId
      },
      UsersError(s"There is a user with the same name: ${user.name}")
    )

  }

  override def getUser(uid: User.Id): Either[UsersError, User] = {

    users.get(uid).toRight(UsersError(s"There is no user with uid: $uid"))

  }

  override def deleteUser(uid: User.Id): Either[Domain.UsersError, Domain.User] = {

    users.remove(uid).toRight(UsersError(s"There is no user with uid: $uid"))

  }
}
