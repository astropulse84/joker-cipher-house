package com.Kawwuxu6.Wodfer.domain.model

data class WitnessSeed(
    val name: String,
    val time: String,
    val truthClaim: String,
    val lieClaim: String
)

data class SuspectSeed(
    val name: String,
    val role: String
)

data class RelationSeed(
    val from: Int,
    val to: Int,
    val label: String
)

data class ChapterData(
    val title: String,
    val premise: String,
    val houseRecord: String,
    val witnesses: List<WitnessSeed>,
    val cipherWords: List<String>,
    val cipherDecoys: List<String>,
    val genuineMark: String,
    val genuineProps: List<String>,
    val forgedProps: List<String>,
    val targetName: String,
    val targetRole: String,
    val pressQuestions: List<String>,
    val deflections: List<String>,
    val suspects: List<SuspectSeed>,
    val relations: List<RelationSeed>,
    val motives: List<String>,
    val trueSuspect: Int,
    val trueMotive: Int,
    val revealTemplate: String,
    val evidenceNames: List<String>
)
