package com.lambdarat.reader

import com.lambdarat.common.Domain.{Group, User}
import com.lambdarat.reader.FuncMeetupImpl._
import com.lambdarat.reader.MeetupImplTypes.{MeetupContext, MeetupOperation}

import cats.data.Reader

object MeetupImplTypes {

  case class MeetupContext(
    getUser: GetUser, createUser: CreateUser, deleteUser: DeleteUser,
    getGroup: GetGroup, saveGroup: SaveGroup, closeGroup: CloseGroup,
    joinUserToGroup: JoinUserToGroup, removeUserFromGroup: RemoveUserFromGroup
  )

  type MeetupOperation[S] = Reader[MeetupContext, S]

}

object MeetupImpl extends Meetup[MeetupOperation] {

  override def registerUser(name: User.Name, age: User.Age) = Reader { ctx =>
    FuncMeetupImpl.registerUser(ctx.createUser)(name, age)
  }

  override def deleteUser(uid: User.Id) = Reader { ctx =>
    FuncMeetupImpl.deleteUser(ctx.deleteUser)(uid)
  }

  override def registerGroup(name: Group.Name) = Reader { ctx =>
    FuncMeetupImpl.registerGroup(ctx.saveGroup)(name)
  }

  override def closeGroup(gid: Group.Id) = Reader { ctx =>
    FuncMeetupImpl.closeGroup(ctx.closeGroup)(gid)
  }

  override def getGroupUsers(gid: Group.Id) = Reader { ctx =>
    FuncMeetupImpl.getGroupUsers(ctx.getGroup)(gid)
  }

  override def joinUserToGroup(uid: User.Id, gid: Group.Id) = Reader { ctx =>
    FuncMeetupImpl.joinUserToGroup(ctx.getUser, ctx.getGroup, ctx.saveGroup, ctx.joinUserToGroup)(uid, gid)
  }

  override def removeUserFromGroup(uid: User.Id, gid: Group.Id) = Reader { ctx =>
    FuncMeetupImpl.removeUserFromGroup(ctx.getUser, ctx.getGroup, ctx.saveGroup, ctx.removeUserFromGroup)(uid, gid)
  }

}
