package com.lambdarat.common

object Domain {

  case class User(name: User.Name, age: User.Age, uid: Option[User.Id] = None)

  object User {

    case class Id(value: Long) extends AnyVal

    case class Name(value: String) extends AnyVal

    case class Age(value: Int) extends AnyVal

  }

  case class Group(name: Group.Name, users: Seq[User], gid: Option[Group.Id] = None)

  object Group {

    case class Id(value: Long) extends AnyVal

    case class Name(value: String) extends AnyVal

  }

  sealed trait MeetupError {

    def err: String

  }

  case class GenericError(err: String) extends MeetupError

  case class UsersError(err: String) extends MeetupError

  case class GroupsError(err: String) extends MeetupError

  case class NotifierError(err: String) extends MeetupError

  sealed trait GroupEvent {

    def uid: User.Id

  }

  object GroupEvent {

    case class UserRemoved(uid: User.Id) extends GroupEvent

    case class UserAdded(uid: User.Id) extends GroupEvent

  }

}
