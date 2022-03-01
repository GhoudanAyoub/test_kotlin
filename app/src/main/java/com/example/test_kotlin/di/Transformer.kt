package com.example.test_kotlin.di

import com.example.test_kotlin.Models.action
import com.example.test_kotlin.Models.users
import com.example.test_kotlin.db.entity.actionEntity
import com.example.test_kotlin.db.entity.usersEntity

object Transformer {

    fun convertUserModelToUserEntity(users: users): usersEntity {
        return usersEntity(
            id = users.id,
            name = users.name,
            username = users.username,
            email = users.email,
            phone = users.phone,
            website = users.website
        )
    }

    fun convertUserEntityToUserModel(usersEntity: usersEntity): users {
        return users(
            id = usersEntity.id,
            name = usersEntity.name,
            username = usersEntity.username,
            email = usersEntity.email,
            phone = usersEntity.phone,
            website = usersEntity.website
        )
    }
    fun convertActionModelToActionEntity(action: action): actionEntity {
        return actionEntity(
            id = action.id,
            userId = action.userId,
            title = action.title,
            completed = action.completed
        )
    }

    fun convertActionEntityToActionModel(actionEntity: actionEntity): action {
        return action(
            id = actionEntity.id,
            userId = actionEntity.userId,
            title = actionEntity.title,
            completed = actionEntity.completed
        )
    }
}