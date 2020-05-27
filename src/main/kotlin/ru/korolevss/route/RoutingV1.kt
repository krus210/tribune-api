package ru.korolevss.route

import io.ktor.application.call
import io.ktor.auth.authenticate
import io.ktor.features.ParameterConversionException
import io.ktor.http.HttpStatusCode
import io.ktor.http.content.files
import io.ktor.http.content.static
import io.ktor.request.receive
import io.ktor.request.receiveMultipart
import io.ktor.response.respond
import io.ktor.routing.*
import io.ktor.util.KtorExperimentalAPI
import ru.korolevss.dto.*
import ru.korolevss.me
import ru.korolevss.service.FCMService
import ru.korolevss.service.FileService
import ru.korolevss.service.PostService
import ru.korolevss.service.UserService

class RoutingV1(
        private val staticPath: String,
        private val postService: PostService,
        private val fileService: FileService,
        private val userService: UserService,
        private val fcmService: FCMService
) {
    @KtorExperimentalAPI
    fun setup(configuration: Routing) {
        with(configuration) {
            route("/api/v1/") {

                static("/static") {
                    files(staticPath)
                }

                route("/") {
                    post("/registration") {
                        val input = call.receive<UserRequestDto>()
                        val response = userService.save(input)
                        call.respond(response)
                    }
                    post("/authentication") {
                        val input = call.receive<UserRequestDto>()
                        val response = userService.authenticate(input)
                        call.respond(response)
                    }
                }

                authenticate("basic", "jwt") {
                    route("/me") {
                        get {
                            call.respond(UserResponseDto.fromModel(me!!.id, userService, postService))
                        }
                        post("/change-password"){
                            val input = call.receive<PasswordChangeRequestDto>()
                            val response = userService.changePassword(me!!.id, input)
                            call.respond(response)
                        }
                        post("/firebase-token"){
                            val token = call.receive<TokenDto>()
                            userService.saveFirebaseToken(me!!.id, token.token)
                            call.respond(HttpStatusCode.OK)
                        }
                    }

                    route("/posts"){
                        get("/recent") {
                            val response = postService.getRecentPosts(me!!.id, userService)
                            call.respond(response)
                        }
                        get("/{id}/before") {
                            val postId = call.parameters["id"]?.toLongOrNull() ?: throw ParameterConversionException(
                                    "id",
                                    "Long"
                            )
                            val response = postService.getPostsBefore(me!!.id, postId, userService)
                            call.respond(response)
                        }
                        post {
                            val input = call.receive<PostRequestDto>()
                            postService.save(me!!.id, input, userService)
                            call.respond(HttpStatusCode.OK)
                        }
                        delete("/{id}") {
                            val postId = call.parameters["id"]?.toLongOrNull() ?: throw ParameterConversionException(
                                    "id",
                                    "Long"
                            )
                            postService.removeById(me!!.id, postId, userService)
                            call.respond(HttpStatusCode.OK)
                        }
                        post("/{id}/like"){
                            val postId = call.parameters["id"]?.toLongOrNull() ?: throw ParameterConversionException(
                                    "id",
                                    "Long"
                            )
                            val response = postService.likeById(me!!.id, postId, userService, fcmService)
                            call.respond(response)
                        }
                        post("/{id}/dislike"){
                            val postId = call.parameters["id"]?.toLongOrNull() ?: throw ParameterConversionException(
                                    "id",
                                    "Long"
                            )
                            val response = postService.dislikeById(me!!.id, postId, userService, fcmService)
                            call.respond(response)
                        }
                        get("/me") {
                            val response = postService.getUserPosts(me!!.id, userService)
                            call.respond(response)
                        }
                        get("/username/{username}") {
                            val username = call.parameters["username"]
                            val user = userService.getByUserName(username!!)
                            val response = postService.getUserPosts(user!!.id, userService)
                            call.respond(response)
                        }
                        get("/{id}/list-like-dislike-users"){
                            val postId = call.parameters["id"]?.toLongOrNull() ?: throw ParameterConversionException(
                                    "id",
                                    "Long"
                            )
                            val response = userService.listUsersLikeDislikePostById(postId, postService)
                            call.respond(response)

                        }
                    }

                    route("/media") {
                        post {
                            val multipart = call.receiveMultipart()
                            val response = fileService.save(multipart)
                            call.respond(response)
                        }
                    }
                }

            }
        }
    }
}