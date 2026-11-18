package com.Kawwuxu6.Wodfer.domain.usecase

import com.Kawwuxu6.Wodfer.core.config.GameConfig
import com.Kawwuxu6.Wodfer.domain.model.Case
import com.Kawwuxu6.Wodfer.domain.model.ChapterData
import com.Kawwuxu6.Wodfer.domain.model.CipherCard
import com.Kawwuxu6.Wodfer.domain.model.PropClue
import com.Kawwuxu6.Wodfer.domain.model.QuestionClue
import com.Kawwuxu6.Wodfer.domain.model.Relation
import com.Kawwuxu6.Wodfer.domain.model.Suspect
import com.Kawwuxu6.Wodfer.domain.model.WitnessClue
import kotlin.random.Random

class GenerateCaseUseCase(private val chapters: List<ChapterData>) {

    operator fun invoke(chapterIndex: Int, attempt: Int): Case {
        val index = chapterIndex.coerceIn(0, chapters.lastIndex)
        val seed = index.toLong() * 7919L xor (attempt.toLong() * 104729L)
        var tries = 0
        while (tries < GameConfig.GENERATION_ATTEMPTS) {
            val candidate = build(index, seed + tries, chapters[index])
            if (isSolvable(candidate)) return candidate
            tries++
        }
        return fallback(index, seed, chapters[index])
    }

    fun previewCase(chapterIndex: Int): Case {
        val index = chapterIndex.coerceIn(0, chapters.lastIndex)
        return fallback(index, 0L, chapters[index])
    }

    private fun build(index: Int, seed: Long, data: ChapterData): Case {
        val random = Random(seed)
        val liar = random.nextInt(data.witnesses.size)
        val witnesses = data.witnesses
            .mapIndexed { position, seedWitness ->
                WitnessClue(
                    name = seedWitness.name,
                    time = seedWitness.time,
                    claim = if (position == liar) seedWitness.lieClaim else seedWitness.truthClaim,
                    contradicts = position == liar
                )
            }
            .shuffled(random)

        val word = data.cipherWords[random.nextInt(data.cipherWords.size)]
        val cards = word.map { letter -> CipherCard(rankLabel(letter - 'A' + 1)) }
        val options = (data.cipherDecoys.shuffled(random).take(2) + word).shuffled(random)

        val forged = data.forgedProps[random.nextInt(data.forgedProps.size)]
        val props = (data.genuineProps.shuffled(random).take(5).map { PropClue(it, false) } +
            PropClue(forged, true)).shuffled(random)

        val press = data.pressQuestions[random.nextInt(data.pressQuestions.size)]
        val questions = (data.deflections.shuffled(random).take(2).map { QuestionClue(it, false) } +
            QuestionClue(press, true)).shuffled(random)

        val positions = NODE_POSITIONS.shuffled(random)
        val suspects = data.suspects.mapIndexed { position, seedSuspect ->
            Suspect(
                name = seedSuspect.name,
                role = seedSuspect.role,
                x = positions[position].first,
                y = positions[position].second
            )
        }

        val motives = data.motives.shuffled(random)

        return Case(
            chapterIndex = index,
            chapterTitle = data.title,
            premise = data.premise,
            seed = seed,
            houseRecord = data.houseRecord,
            witnesses = witnesses,
            cards = cards,
            cipherOptions = options,
            cipherAnswer = options.indexOf(word),
            genuineMark = data.genuineMark,
            props = props,
            targetName = data.targetName,
            targetRole = data.targetRole,
            questions = questions,
            suspects = suspects,
            relations = data.relations.map { Relation(it.from, it.to, it.label) },
            motives = motives,
            trueSuspect = data.trueSuspect,
            trueMotive = motives.indexOf(data.motives[data.trueMotive]),
            reveal = String.format(
                data.revealTemplate,
                data.suspects[data.trueSuspect].name,
                data.motives[data.trueMotive]
            ),
            evidenceNames = data.evidenceNames
        )
    }

    private fun fallback(index: Int, seed: Long, data: ChapterData): Case {
        val witnesses = data.witnesses.mapIndexed { position, seedWitness ->
            WitnessClue(
                name = seedWitness.name,
                time = seedWitness.time,
                claim = if (position == 0) seedWitness.lieClaim else seedWitness.truthClaim,
                contradicts = position == 0
            )
        }
        val word = data.cipherWords.first()
        val options = listOf(word) + data.cipherDecoys.take(2)
        val props = data.genuineProps.take(5).map { PropClue(it, false) } +
            PropClue(data.forgedProps.first(), true)
        val questions = listOf(QuestionClue(data.pressQuestions.first(), true)) +
            data.deflections.take(2).map { QuestionClue(it, false) }
        val suspects = data.suspects.mapIndexed { position, seedSuspect ->
            Suspect(
                name = seedSuspect.name,
                role = seedSuspect.role,
                x = NODE_POSITIONS[position].first,
                y = NODE_POSITIONS[position].second
            )
        }
        return Case(
            chapterIndex = index,
            chapterTitle = data.title,
            premise = data.premise,
            seed = seed,
            houseRecord = data.houseRecord,
            witnesses = witnesses,
            cards = word.map { letter -> CipherCard(rankLabel(letter - 'A' + 1)) },
            cipherOptions = options,
            cipherAnswer = 0,
            genuineMark = data.genuineMark,
            props = props,
            targetName = data.targetName,
            targetRole = data.targetRole,
            questions = questions,
            suspects = suspects,
            relations = data.relations.map { Relation(it.from, it.to, it.label) },
            motives = data.motives,
            trueSuspect = data.trueSuspect,
            trueMotive = data.trueMotive,
            reveal = String.format(
                data.revealTemplate,
                data.suspects[data.trueSuspect].name,
                data.motives[data.trueMotive]
            ),
            evidenceNames = data.evidenceNames
        )
    }

    private fun isSolvable(case: Case): Boolean {
        if (case.witnesses.count { it.contradicts } != 1) return false
        if (case.props.count { it.forged } != 1) return false
        if (case.questions.count { it.presses } != 1) return false
        if (case.cipherAnswer !in case.cipherOptions.indices) return false
        if (case.cipherOptions.distinct().size != case.cipherOptions.size) return false
        if (case.props.map { it.descriptor }.distinct().size != case.props.size) return false
        if (case.witnesses.map { it.claim }.distinct().size != case.witnesses.size) return false
        if (case.cards.size != 5) return false
        if (case.evidenceNames.size != GameConfig.STAGES) return false
        if (case.motives.size < 3) return false
        if (case.trueMotive !in case.motives.indices) return false
        val links = case.relations.count { it.from == case.trueSuspect || it.to == case.trueSuspect }
        return links >= 2
    }

    private fun rankLabel(value: Int): String = when (value) {
        1 -> "A"
        10 -> "J"
        11 -> "Q"
        12 -> "K"
        else -> value.toString()
    }

    private companion object {
        val NODE_POSITIONS = listOf(
            0.22f to 0.22f,
            0.78f to 0.26f,
            0.26f to 0.74f,
            0.74f to 0.78f
        )
    }
}
