package com.lambdarat.common.services

import java.util.concurrent.atomic.AtomicLong

import com.lambdarat.common.Domain.{Group, GroupsError}

import scala.collection.concurrent.TrieMap

class GroupsImpl extends Groups {

  private val groups = TrieMap.empty[Group.Id, Group]

  private val groupIdGenerator = new AtomicLong(0)

  private def groupNotFound(gid: Group.Id) = GroupsError(s"There is no group with the gid: $gid")

  override def saveGroup(group: Group): Either[GroupsError, Group.Id] = {

    lazy val groupWithSameName = GroupsError(s"There is a group with the same name: ${group.name}")

    def groupWithSameId(gid: Group.Id) = GroupsError(s"There is a group with the same gid: $gid")

    group.gid match {

      case Some(id) => {

        for {
          _ <- Either.cond(!groups.values.exists(g => g.name == group.name && g.gid != group.gid), (), groupWithSameName)
          _ <- groups.replace(id, group).toRight(groupNotFound(id))
        } yield {
          id
        }

      }
      case None     => {

        import cats.syntax.either._

        for {
          _   <- Either.cond(!groups.values.exists(_.name == group.name), (), groupWithSameName)
          gid  = Group.Id(groupIdGenerator.incrementAndGet())
          _   <- groups.putIfAbsent(gid, group.copy(gid = Some(gid))).toRight(group).swap.leftMap(_ => groupWithSameId(gid))
        } yield {
          gid
        }

      }

    }

  }

  override def getGroup(gid: Group.Id): Either[GroupsError, Group] = {

    groups.get(gid).toRight(groupNotFound(gid))

  }

  override def closeGroup(gid: Group.Id): Either[GroupsError, Group] = {

    groups.remove(gid).toRight(groupNotFound(gid))

  }



}
