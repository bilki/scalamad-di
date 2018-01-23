package com.lambdarat.cake
import com.lambdarat.common.services.{Users, UsersImpl}

trait UsersComponentImpl extends UsersComponent {

  override val users: Users = new UsersImpl

}
