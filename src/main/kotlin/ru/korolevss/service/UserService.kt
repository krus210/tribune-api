package ru.korolevss.service

import io.ktor.features.NotFoundException
import io.ktor.util.KtorExperimentalAPI
import org.springframework.security.crypto.password.PasswordEncoder
import ru.korolevss.dto.*
import ru.korolevss.exception.InvalidPasswordException
import ru.korolevss.exception.NullUsernameOrPasswordException
import ru.korolevss.exception.PasswordChangeException
import ru.korolevss.exception.UserExistsException
import ru.korolevss.model.*
import ru.korolevss.repository.PostRepository
import ru.korolevss.repository.UserRepository

class UserService(
        private val repo: UserRepository,
        private val tokenService: JWTTokenService,
        private val passwordEncoder: PasswordEncoder) {

    suspend fun getModelByIdPassword(id: Long, password: String): UserModel? {
        return repo.getByIdPassword(id, password)
    }

    @KtorExperimentalAPI
    suspend fun getById(userId: Long) =
            repo.getById(userId) ?: throw NotFoundException()

    @KtorExperimentalAPI
    suspend fun getByUserName(username: String): UserModel? {
        return repo.getByUsername(username) ?: throw NotFoundException()
    }

    suspend fun save(input: UserRequestDto): TokenDto {
        if (input.name == "" || input.password == "") {
            throw NullUsernameOrPasswordException("Username or password is empty")
        } else if (repo.getByUsername(input.name) != null) {
            throw UserExistsException("User already exists")
        } else {
            val user = repo.save(UserModel(
                    id = 0,
                    name = input.name,
                    password = passwordEncoder.encode(input.password)
            ))
            val token = tokenService.generate(user)
            return TokenDto(token)
        }
    }

    @KtorExperimentalAPI
    suspend fun addImage(userId: Long, mediaModel: MediaModel) {
        val user = repo.getById(userId) ?: throw NotFoundException()
        repo.addImage(user, mediaModel)
    }

    @KtorExperimentalAPI
    suspend fun authenticate(input: UserRequestDto): TokenDto {
        val user = repo.getByUsername(input.name) ?: throw NotFoundException()
        if (!passwordEncoder.matches(input.password, user.password)) {
            throw InvalidPasswordException("Wrong password!")
        }
        val token = tokenService.generate(user)
        return TokenDto(token)
    }

    @KtorExperimentalAPI
    suspend fun changePassword(userId: Long, input: PasswordChangeRequestDto): TokenDto {
        val user = getById(userId)
        if (!passwordEncoder.matches(input.old, user.password)) {
            throw PasswordChangeException("Wrong password!")
        }
        repo.update(user.id, passwordEncoder.encode(input.new))
        val token = tokenService.generate(user)
        return TokenDto(token)
    }

    @KtorExperimentalAPI
    suspend fun saveFirebaseToken(id: Long, firebaseToken: String) = repo.saveFirebaseToken(id, firebaseToken)
            ?: throw NotFoundException()

    @KtorExperimentalAPI
    suspend fun addLike(userId: Long) = repo.addLike(userId) ?: throw NotFoundException()

    @KtorExperimentalAPI
    suspend fun addDislike(userId: Long) = repo.addDislike(userId) ?: throw NotFoundException()

    @KtorExperimentalAPI
    suspend fun checkReadOnly(userId: Long, postService: PostService): Boolean {
        return repo.checkReadOnly(userId, postService)
    }

    @KtorExperimentalAPI
    suspend fun checkStatus(userId: Long): UserStatus {
        val user = getById(userId)
        return repo.checkStatus(user)
    }

    @KtorExperimentalAPI
    suspend fun addPostId(userId: Long, postId: Long) {
        val user = getById(userId)
        repo.addPostId(user, postId)
    }

    @KtorExperimentalAPI
    suspend fun removePostId(userId: Long, postId: Long) {
        val user = getById(userId)
        repo.removePostId(user, postId)
    }

    @KtorExperimentalAPI
    suspend fun listUsersLikeDislikePostById(postId: Long, postService: PostService): List<LikeDislikeDto>{
        val post = postService.getPostById(postId)
        return repo.listUsersLikeDislikePostById(post).map { LikeDislikeDto.fromModel(it, this) }
    }

}
