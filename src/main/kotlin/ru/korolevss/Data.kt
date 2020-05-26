package ru.korolevss

import ru.korolevss.model.PostModel
import ru.korolevss.model.UserModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

val users = listOf(
        UserModel(1, "user1", "123456", mutableListOf(25,23,21,19,17), "user1", likes = 9, dislikes = 0),
        UserModel(2, "user2", "123456", mutableListOf(24,22,20,18,16), "user2", likes = 12, dislikes = 0),
        UserModel(3, "user3", "123456", mutableListOf(15,13,11,9,7), "user3", likes = 7, dislikes = 0),
        UserModel(4, "user4", "123456", mutableListOf(14,12,10,8,6), "user4", likes = 6, dislikes = 1),
        UserModel(5, "user5", "123456", mutableListOf(5), "user5", likes = 9, dislikes = 4),
        UserModel(6, "user6", "123456", mutableListOf(4), "user6", likes = 3, dislikes = 7),
        UserModel(7, "user7", "123456", mutableListOf(3), "user7", likes = 3, dislikes = 8),
        UserModel(8, "user8", "123456", mutableListOf(2), likes = 1, dislikes = 9),
        UserModel(9, "user9", "123456", mutableListOf(1), likes = 1, dislikes = 8),
        UserModel(10, "user10", "123456", mutableListOf(), likes = 0, dislikes = 12),
        UserModel(11, "user11", "123456", mutableListOf(), likes = 0, dislikes = 14)

)

val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
val dates0 = listOf(
        LocalDateTime.parse("2020-04-05 12:30", formatter),
        LocalDateTime.parse("2020-04-05 12:45", formatter),
        LocalDateTime.parse("2020-04-05 13:30", formatter),
        LocalDateTime.parse("2020-04-05 14:30", formatter),
        LocalDateTime.parse("2020-04-05 15:30", formatter),
        LocalDateTime.parse("2020-04-05 16:30", formatter),
        LocalDateTime.parse("2020-04-06 12:30", formatter),
        LocalDateTime.parse("2020-04-07 12:30", formatter),
        LocalDateTime.parse("2020-04-07 13:30", formatter),
        LocalDateTime.parse("2020-04-07 14:30", formatter),
        LocalDateTime.parse("2020-04-07 15:30", formatter),
        LocalDateTime.parse("2020-04-08 12:30", formatter),
        LocalDateTime.parse("2020-04-08 13:30", formatter),
        LocalDateTime.parse("2020-04-09 14:30", formatter),
        LocalDateTime.parse("2020-04-10 14:30", formatter),
        LocalDateTime.parse("2020-04-11 14:30", formatter),
        LocalDateTime.parse("2020-04-12 14:30", formatter),
        LocalDateTime.parse("2020-04-13 14:30", formatter),
        LocalDateTime.parse("2020-04-14 14:30", formatter),
        LocalDateTime.parse("2020-04-15 14:30", formatter),
        LocalDateTime.parse("2020-04-16 14:30", formatter),
        LocalDateTime.parse("2020-04-17 14:30", formatter),
        LocalDateTime.parse("2020-04-18 14:30", formatter),
        LocalDateTime.parse("2020-04-19 14:30", formatter),
        LocalDateTime.parse("2020-04-20 14:30", formatter)

)
val dates1 = listOf(
        LocalDateTime.parse("2020-05-05 12:30", formatter),
        LocalDateTime.parse("2020-05-05 12:45", formatter),
        LocalDateTime.parse("2020-05-05 13:30", formatter),
        LocalDateTime.parse("2020-05-05 14:30", formatter),
        LocalDateTime.parse("2020-05-05 15:30", formatter),
        LocalDateTime.parse("2020-05-05 16:30", formatter),
        LocalDateTime.parse("2020-05-06 12:30", formatter),
        LocalDateTime.parse("2020-05-07 12:30", formatter),
        LocalDateTime.parse("2020-05-07 13:30", formatter),
        LocalDateTime.parse("2020-05-07 14:30", formatter),
        LocalDateTime.parse("2020-05-07 15:30", formatter),
        LocalDateTime.parse("2020-05-08 12:30", formatter),
        LocalDateTime.parse("2020-05-08 13:30", formatter),
        LocalDateTime.parse("2020-05-09 14:30", formatter),
        LocalDateTime.parse("2020-05-10 14:30", formatter),
        LocalDateTime.parse("2020-05-11 14:30", formatter),
        LocalDateTime.parse("2020-05-12 14:30", formatter),
        LocalDateTime.parse("2020-05-13 14:30", formatter),
        LocalDateTime.parse("2020-05-14 14:30", formatter),
        LocalDateTime.parse("2020-05-15 14:30", formatter),
        LocalDateTime.parse("2020-05-16 14:30", formatter),
        LocalDateTime.parse("2020-05-17 14:30", formatter),
        LocalDateTime.parse("2020-05-18 14:30", formatter),
        LocalDateTime.parse("2020-05-19 14:30", formatter),
        LocalDateTime.parse("2020-05-20 14:30", formatter)

)

val posts = listOf(
        PostModel(25, users[0], dates0[24], "Post 25 from user 1", "post25",
                likeUsersId = mutableMapOf(2L to dates1[0], 3L to dates1[1], 4L to dates1[2], 5L to dates1[3], 6L to dates1[4], 7L to dates1[5], 8L to dates1[6], 9L to dates1[7]),
                dislikeUsersId = mutableMapOf(10L to dates1[8], 11L to dates1[9])
        ),
        PostModel(23, users[0], dates0[22], "Post 23 from user 1", "post23",
                likeUsersId = mutableMapOf(2L to dates1[1], 3L to dates1[3], 4L to dates1[4], 5L to dates1[15]),
                dislikeUsersId = mutableMapOf(10L to dates1[14], 11L to dates1[16], 6L to dates1[6], 7L to dates1[8], 8L to dates1[20], 9L to dates1[19])
        ),
        PostModel(21, users[0], dates0[20], "Post 21 from user 1", "post21",
                likeUsersId = mutableMapOf(2L to dates1[4], 3L to dates1[5], 4L to dates1[6], 5L to dates1[7], 6L to dates1[8]),
                dislikeUsersId = mutableMapOf(10L to dates1[9], 11L to dates1[10], 7L to dates1[11], 8L to dates1[8], 9L to dates1[12])
        ),
        PostModel(19, users[0], dates0[18], "Post 19 from user 1", "post19",
                likeUsersId = mutableMapOf(2L to dates1[15], 3L to dates1[16], 4L to dates1[17], 5L to dates1[18]),
                dislikeUsersId = mutableMapOf(10L to dates1[2], 11L to dates1[0], 7L to dates1[1], 8L to dates1[2], 9L to dates1[12])
        ),
        PostModel(17, users[0], dates0[16], "Post 17 from user 1", "post17",
                likeUsersId = mutableMapOf(2L to dates1[4], 3L to dates1[5], 4L to dates1[6], 5L to dates1[7]),
                dislikeUsersId = mutableMapOf(10L to dates1[9], 11L to dates1[10], 7L to dates1[11], 8L to dates1[8], 9L to dates1[12])
        ),
        PostModel(24, users[1], dates0[23], "Post 24 from user 2", "post24",
                dislikeUsersId = mutableMapOf(10L to dates1[9], 11L to dates1[10], 7L to dates1[11], 8L to dates1[8], 9L to dates1[12])
        ),
        PostModel(22, users[1], dates0[21], "Post 22 from user 2", "post22",
                dislikeUsersId = mutableMapOf(5L to dates1[15], 6L to dates1[16], 7L to dates1[17], 8L to dates1[8], 9L to dates1[0], 10L to dates1[4], 11L to dates1[5])
        ),
        PostModel(20, users[1], dates0[19], "Post 20 from user 2", "post20",
                likeUsersId = mutableMapOf(1L to dates1[20], 3L to dates1[15])
        ),
        PostModel(18, users[1], dates0[17], "Post 18 from user 2", "post18",
                dislikeUsersId = mutableMapOf(5L to dates1[15], 6L to dates1[16])
        ),
        PostModel(16, users[1], dates0[15], "Post 16 from user 2", "post16",
                likeUsersId = mutableMapOf(5L to dates1[1]),
                dislikeUsersId = mutableMapOf(6L to dates1[16], 7L to dates1[17], 8L to dates1[8], 9L to dates1[0], 10L to dates1[4], 11L to dates1[5])
        ),
        PostModel(15, users[2], dates0[14], "Post 15 from user 3", "post15",
                likeUsersId = mutableMapOf(1L to dates1[15], 2L to dates1[9], 5L to dates1[1]),
                dislikeUsersId = mutableMapOf(11L to dates1[10])
        ),
        PostModel(13, users[2], dates0[12], "Post 13 from user 3", "post13",
                likeUsersId = mutableMapOf(1L to dates1[1], 2L to dates1[2], 4L to dates1[3])
        ),
        PostModel(11, users[2], dates0[10], "Post 11 from user 3", "post11",
                likeUsersId = mutableMapOf(1L to dates1[15], 2L to dates1[9], 5L to dates1[1], 7L to dates1[20]),
                dislikeUsersId = mutableMapOf(6L to dates1[10], 11L to dates1[8])
        ),
        PostModel(9, users[2], dates0[8], "Post 9 from user 3", "post9",
                likeUsersId = mutableMapOf(1L to dates1[15], 2L to dates1[9], 5L to dates1[1], 7L to dates1[20]),
                dislikeUsersId = mutableMapOf(6L to dates1[10], 11L to dates1[8])
        ),
        PostModel(7, users[2], dates0[6], "Post 7 from user 3", "post7",
                likeUsersId = mutableMapOf(1L to dates1[0], 2L to dates1[10])
        ),
        PostModel(14, users[3], dates0[13], "Post 14 from user 4", "post14",
                dislikeUsersId = mutableMapOf(4L to dates1[10], 5L to dates1[8],7L to dates1[3], 8L to dates1[23], 10L to dates1[15], 11L to dates1[15])
        ),
        PostModel(12, users[3], dates0[11], "Post 12 from user 4", "post12",
                dislikeUsersId = mutableMapOf(10L to dates1[3], 11L to dates1[2])
        ),
        PostModel(10, users[3], dates0[9], "Post 10 from user 4", "post10",
                likeUsersId = mutableMapOf(1L to dates1[4], 2L to dates1[2]),
                dislikeUsersId = mutableMapOf(6L to dates1[10], 10L to dates1[15], 11L to dates1[9])
        ),
        PostModel(8, users[3], dates0[7], "Post 8 from user 4", "post8",
                likeUsersId = mutableMapOf(1L to dates1[19], 2L to dates1[17], 3L to dates1[4], 6L to dates1[18]),
                dislikeUsersId = mutableMapOf(5L to dates1[10])
        ),
        PostModel(6, users[3], dates0[5], "Post 6 from user 4", "post6",
                dislikeUsersId = mutableMapOf(5L to dates1[1], 8L to dates1[20], 9L to dates1[2], 10L to dates1[15], 11L to dates1[9])
        ),
        PostModel(5, users[4], dates0[4], "Post 5 from user 5", "post5",
                likeUsersId = mutableMapOf(1L to dates1[23])
                ),
        PostModel(4, users[5], dates0[3], "Post 4 from user 6", "post4"),
        PostModel(3, users[6], dates0[2], "Post 3 from user 7", "post3"),
        PostModel(2, users[7], dates0[1], "Post 2 from user 8", "post2"),
        PostModel(1, users[8], dates0[0], "Post 1 from user 9", "post1")
        )



