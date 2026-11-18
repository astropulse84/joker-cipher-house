package com.Kawwuxu6.Wodfer.domain.model

enum class StageKind { WITNESS, CIPHER, PROPS, INTERROGATION }

data class WitnessClue(
    val name: String,
    val time: String,
    val claim: String,
    val contradicts: Boolean
)

data class CipherCard(val rank: String)

data class PropClue(
    val descriptor: String,
    val forged: Boolean
)

data class QuestionClue(
    val question: String,
    val presses: Boolean
)

data class Suspect(
    val name: String,
    val role: String,
    val x: Float,
    val y: Float
)

data class Relation(
    val from: Int,
    val to: Int,
    val label: String
)

data class Case(
    val chapterIndex: Int,
    val chapterTitle: String,
    val premise: String,
    val seed: Long,
    val houseRecord: String,
    val witnesses: List<WitnessClue>,
    val cards: List<CipherCard>,
    val cipherOptions: List<String>,
    val cipherAnswer: Int,
    val genuineMark: String,
    val props: List<PropClue>,
    val targetName: String,
    val targetRole: String,
    val questions: List<QuestionClue>,
    val suspects: List<Suspect>,
    val relations: List<Relation>,
    val motives: List<String>,
    val trueSuspect: Int,
    val trueMotive: Int,
    val reveal: String,
    val evidenceNames: List<String>
)

enum class EvidenceState { SEALED, VERIFIED, UNVERIFIED }

data class Evidence(
    val name: String,
    val state: EvidenceState
)

enum class SuspectMark { NONE, SUSPECT, CLEARED }

data class StageOutcome(
    val correct: Boolean,
    val evidenceSlot: Int,
    val revealedRelation: Int
)
