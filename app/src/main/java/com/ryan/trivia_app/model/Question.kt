/*
 * Copyright 2022 Ryan Chung Kam Chung
 *
 * Data class that holds relevant information about trivia questions.
 */

package com.ryan.trivia_app.model

/**
 * Parsed data of all relevant info of a question.
 *
 * @property question the question.
 * @property rightAnswer the right answer.
 * @property wrongAnswer1 the 1st wrong answer.
 * @property wrongAnswer2 the 2nd wrong answer.
 * @property wrongAnswer3 the 3rd wrong answer.
 */
data class Question(
    val question: String,
    val rightAnswer: String,
    val wrongAnswer1: String,
    val wrongAnswer2: String,
    val wrongAnswer3: String
)
