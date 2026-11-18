package com.Kawwuxu6.Wodfer.data.sample

import com.Kawwuxu6.Wodfer.domain.model.ChapterData
import com.Kawwuxu6.Wodfer.domain.model.RelationSeed
import com.Kawwuxu6.Wodfer.domain.model.SuspectSeed
import com.Kawwuxu6.Wodfer.domain.model.WitnessSeed

object SampleData {

    val chapters: List<ChapterData> = listOf(
        ChapterData(
            title = "THE CURTAIN CALL",
            premise = "A PROMPTER IS FOUND BEHIND THE FALLEN CURTAIN AND THE HOUSE CLOSES ITS DOORS.",
            houseRecord = "THE CURTAIN FELL AT 21:10.",
            witnesses = listOf(
                WitnessSeed(
                    name = "MADAME VOSS",
                    time = "20:45",
                    truthClaim = "I LEFT THE WINGS LONG BEFORE THE CURTAIN CAME DOWN.",
                    lieClaim = "THE CURTAIN WAS ALREADY DOWN WHEN I REACHED THE WINGS AT 20:45."
                ),
                WitnessSeed(
                    name = "THE LAMPLIGHTER",
                    time = "21:05",
                    truthClaim = "I TRIMMED THE BRASS LAMPS FIVE MINUTES BEFORE THE FALL.",
                    lieClaim = "I TRIMMED THE LAMPS AT 21:05, AN HOUR AFTER THE CURTAIN FELL."
                ),
                WitnessSeed(
                    name = "CLERK OF THE BOX",
                    time = "21:10",
                    truthClaim = "I HEARD THE CURTAIN DROP AS I CLOSED THE LEDGER.",
                    lieClaim = "I CLOSED THE LEDGER AT 21:10, A FULL HOUR AFTER THE FALL."
                ),
                WitnessSeed(
                    name = "THE PROMPTER BOY",
                    time = "21:20",
                    truthClaim = "I FOUND THE WINGS EMPTY TEN MINUTES AFTER THE FALL.",
                    lieClaim = "AT 21:20 THE CURTAIN HAD STILL NOT COME DOWN."
                )
            ),
            cipherWords = listOf("ALIBI", "CABLE", "FABLE"),
            cipherDecoys = listOf("LEDGE", "BADGE", "CHIEF", "FIELD"),
            genuineMark = "EVERY HOUSE PROP CARRIES A GOLD DOUBLE RULE ON ITS BASE.",
            genuineProps = listOf(
                "A LACQUERED CANE WITH TWO GOLD LINES AT THE FOOT.",
                "A BRASS LAMP RINGED TWICE IN GOLD BENEATH THE WICK.",
                "AN IVORY FAN WITH A DOUBLE GOLD RULE ALONG ITS SPINE.",
                "A VELVET FOOTSTOOL BANDED BY TWO GOLD LINES.",
                "A SILVER TRAY WITH TWIN GOLD RULES UNDER THE RIM.",
                "A WOODEN CROWN WITH TWO GOLD LINES INSIDE THE BAND."
            ),
            forgedProps = listOf(
                "A LACQUERED CANE MARKED WITH A SINGLE GOLD LINE.",
                "A BRASS LAMP WITH THREE GOLD RINGS AND NO RULE.",
                "AN IVORY FAN STAMPED WITH A SILVER RULE, NOT GOLD."
            ),
            targetName = "THE UNDERSTUDY",
            targetRole = "SECOND LEAD OF THE HOUSE",
            pressQuestions = listOf(
                "WHO HELD THE ROPE WHEN THE CURTAIN CAME DOWN?",
                "WHY WAS YOUR NAME INKED OVER THE BILLING SHEET?"
            ),
            deflections = listOf(
                "DID YOU ENJOY THE OVERTURE THAT NIGHT?",
                "HOW LONG HAVE YOU WORN THAT VELVET COAT?",
                "IS THE HOUSE COFFEE STILL AS BITTER?"
            ),
            suspects = listOf(
                SuspectSeed("THE IMPRESARIO", "OWNER OF THE HOUSE"),
                SuspectSeed("THE UNDERSTUDY", "SECOND LEAD OF THE HOUSE"),
                SuspectSeed("THE CARD PRINTER", "KEEPER OF THE PRESS"),
                SuspectSeed("THE HOUSE DOCTOR", "PHYSICIAN OF THE STAGE")
            ),
            relations = listOf(
                RelationSeed(0, 1, "RIVAL"),
                RelationSeed(1, 2, "DEBT"),
                RelationSeed(0, 3, "ALIBI"),
                RelationSeed(2, 3, "MENTOR")
            ),
            motives = listOf("THE EMPTY BILLING", "THE SILENCED DEBT", "THE STOLEN ENCORE"),
            trueSuspect = 1,
            trueMotive = 0,
            revealTemplate = "%1\$s CUT THE CURTAIN ROPE AND LET THE HOUSE BLAME THE WEATHER. THE MOTIVE WAS %2\$s, AND THE CURTAIN CALL LIE GAVE THE HAND AWAY.",
            evidenceNames = listOf(
                "THE CURTAIN CALL LIE",
                "THE RANK ROW ON THE DECK",
                "THE FORGED HOUSE PROP",
                "THE WINGS ADMISSION"
            )
        ),
        ChapterData(
            title = "THE MARKED DECK",
            premise = "THE HOUSE DECK RETURNS FROM THE PRINTER EXACTLY ONE CARD SHORT.",
            houseRecord = "THE DECK LEFT THE PRINTER COMPLETE AT 12:00.",
            witnesses = listOf(
                WitnessSeed(
                    name = "THE NIGHT PRINTER",
                    time = "11:40",
                    truthClaim = "I COUNTED FIFTY TWO CARDS BEFORE THE CASE WENT OUT.",
                    lieClaim = "THE DECK WAS ALREADY ONE CARD SHORT WHEN IT LEFT ME."
                ),
                WitnessSeed(
                    name = "THE DOOR KEEPER",
                    time = "12:05",
                    truthClaim = "I SEALED THE CASE FIVE MINUTES AFTER IT LEFT THE PRESS.",
                    lieClaim = "NO DECK PASSED MY DOOR AT NOON OR ANYWHERE NEAR IT."
                ),
                WitnessSeed(
                    name = "THE HOUSE DEALER",
                    time = "12:30",
                    truthClaim = "THE CASE WAS STILL SEALED WHEN IT REACHED MY TABLE.",
                    lieClaim = "THAT DECK NEVER WENT TO THE PRESS AT ALL THIS WEEK."
                ),
                WitnessSeed(
                    name = "THE LEDGER CLERK",
                    time = "13:00",
                    truthClaim = "I ENTERED THE DELIVERY AN HOUR AFTER IT LEFT THE PRESS.",
                    lieClaim = "MY LEDGER SHOWS THE DECK STILL AT THE PRESS AT 13:00."
                )
            ),
            cipherWords = listOf("BLACK", "CHALK", "FLECK"),
            cipherDecoys = listOf("EAGLE", "IDEAL", "BIBLE", "GABLE"),
            genuineMark = "EVERY HOUSE CARD BACK HOLDS A RUBY FILIGREE CORNER.",
            genuineProps = listOf(
                "A CARD BACK WITH RUBY FILIGREE IN THE UPPER CORNER.",
                "A CUT CARD EDGED IN RUBY FILIGREE AT THE CORNER.",
                "A JOKER CARRYING THE SAME RUBY CORNER FILIGREE.",
                "A COURT CARD SHOWING RUBY FILIGREE AT THE CORNER.",
                "A SPARE ACE WITH RUBY FILIGREE WORKED INTO THE CORNER.",
                "A HOUSE BLANK BEARING THE RUBY CORNER FILIGREE."
            ),
            forgedProps = listOf(
                "A CARD BACK WITH GOLD FILIGREE WHERE RUBY SHOULD BE.",
                "A JOKER WHOSE CORNER IS BARE OF ANY FILIGREE.",
                "A COURT CARD WITH FILIGREE ALONG THE EDGE, NOT THE CORNER."
            ),
            targetName = "THE CARD PRINTER",
            targetRole = "KEEPER OF THE PRESS",
            pressQuestions = listOf(
                "WHO PAID YOU TO RUN A SECOND PLATE THAT NIGHT?",
                "WHY DOES YOUR LEDGER SKIP THE MIDNIGHT RUN?"
            ),
            deflections = listOf(
                "DO YOU PREFER LINEN STOCK OR PLATE STOCK?",
                "HOW IS THE OLD PRESS BEHAVING THIS WINTER?",
                "WAS THE RUBY INK DELIVERED ON TIME?"
            ),
            suspects = listOf(
                SuspectSeed("THE CARD PRINTER", "KEEPER OF THE PRESS"),
                SuspectSeed("THE HOUSE DEALER", "MASTER OF THE TABLE"),
                SuspectSeed("THE LEDGER CLERK", "KEEPER OF ACCOUNTS"),
                SuspectSeed("THE DOOR KEEPER", "WATCH OF THE STAGE DOOR")
            ),
            relations = listOf(
                RelationSeed(0, 2, "DEBT"),
                RelationSeed(0, 1, "RIVAL"),
                RelationSeed(1, 3, "ALIBI"),
                RelationSeed(2, 3, "MENTOR")
            ),
            motives = listOf("THE SILENT DEBT", "THE SECOND PRESS", "THE BOUGHT SEAT"),
            trueSuspect = 0,
            trueMotive = 1,
            revealTemplate = "%1\$s RAN A SECOND PLATE AFTER MIDNIGHT AND KEPT ONE CARD BACK. THE MOTIVE WAS %2\$s, AND THE RUBY CORNER THAT WAS NEVER THERE PROVED IT.",
            evidenceNames = listOf(
                "THE NOON DELIVERY LIE",
                "THE RANK ROW ON THE BACKS",
                "THE BARE CORNER CARD",
                "THE PRESS ROOM ADMISSION"
            )
        ),
        ChapterData(
            title = "THE IVORY PROP",
            premise = "THE ROYAL SCEPTRE IS SWAPPED FOR A COPY BETWEEN TWO ACTS.",
            houseRecord = "THE SCEPTRE WAS LOCKED IN THE PROP ROOM AT 20:50.",
            witnesses = listOf(
                WitnessSeed(
                    name = "THE PROP MASTER",
                    time = "20:50",
                    truthClaim = "I TURNED THE KEY MYSELF AS THE INTERVAL BELL RANG.",
                    lieClaim = "AT 20:50 THE SCEPTRE WAS STILL IN MY HANDS ON STAGE."
                ),
                WitnessSeed(
                    name = "THE STAGE SWEEP",
                    time = "21:05",
                    truthClaim = "THE PROP ROOM WAS LOCKED WHEN I SWEPT THE WINGS.",
                    lieClaim = "I CARRIED THE SCEPTRE THROUGH THE WINGS AT 20:50."
                ),
                WitnessSeed(
                    name = "WARDROBE MISTRESS",
                    time = "20:55",
                    truthClaim = "I PASSED THE LOCKED DOOR ON MY WAY TO THE RAILS.",
                    lieClaim = "THE PROP ROOM STOOD OPEN AND EMPTY AT 20:50."
                ),
                WitnessSeed(
                    name = "THE BOX ATTENDANT",
                    time = "20:30",
                    truthClaim = "I SAW THE SCEPTRE LEAVE THE STAGE BEFORE THE BELL.",
                    lieClaim = "THE SCEPTRE WAS OUT IN THE HOUSE SEATS AT 20:50."
                )
            ),
            cipherWords = listOf("FAKED", "BLADE", "IDEAL"),
            cipherDecoys = listOf("CHIEF", "FABLE", "LEDGE", "GLIDE"),
            genuineMark = "EVERY IVORY PIECE OF THE HOUSE IS CUT WITH A SEVEN POINT CROWN.",
            genuineProps = listOf(
                "AN IVORY SCEPTRE HEAD CUT WITH A SEVEN POINT CROWN.",
                "AN IVORY MASK WHOSE CROWN SHOWS SEVEN POINTS.",
                "AN IVORY HANDLE CARVED WITH A SEVEN POINT CROWN.",
                "AN IVORY RING BEARING THE SEVEN POINT CROWN.",
                "AN IVORY PLAQUE STAMPED WITH A SEVEN POINT CROWN.",
                "AN IVORY CLASP CUT TO A SEVEN POINT CROWN."
            ),
            forgedProps = listOf(
                "AN IVORY SCEPTRE HEAD WITH A FIVE POINT CROWN.",
                "AN IVORY MASK WHOSE CROWN CARRIES NINE POINTS.",
                "AN IVORY HANDLE WITH A PLAIN BAND AND NO CROWN."
            ),
            targetName = "THE PROP MASTER",
            targetRole = "KEEPER OF THE PROP ROOM",
            pressQuestions = listOf(
                "WHO ELSE HOLDS A KEY TO THE PROP ROOM?",
                "WHY WAS THE SPARE SCEPTRE ALREADY IN THE WINGS?"
            ),
            deflections = listOf(
                "HOW MANY ACTS DOES THE PIECE RUN?",
                "IS THE IVORY POLISHED EVERY NIGHT?",
                "DO YOU FIND THE HOUSE COLD IN WINTER?"
            ),
            suspects = listOf(
                SuspectSeed("THE PROP MASTER", "KEEPER OF THE PROP ROOM"),
                SuspectSeed("WARDROBE MISTRESS", "MISTRESS OF THE RAILS"),
                SuspectSeed("THE STAGE SWEEP", "KEEPER OF THE WINGS"),
                SuspectSeed("THE IMPRESARIO", "OWNER OF THE HOUSE")
            ),
            relations = listOf(
                RelationSeed(0, 3, "DEBT"),
                RelationSeed(0, 1, "MENTOR"),
                RelationSeed(1, 2, "ALIBI"),
                RelationSeed(2, 3, "RIVAL")
            ),
            motives = listOf("THE SOLD RELIC", "THE OLD GRUDGE", "THE QUIET SWAP"),
            trueSuspect = 0,
            trueMotive = 0,
            revealTemplate = "%1\$s SOLD THE REAL SCEPTRE AND SET A COPY IN ITS PLACE. THE MOTIVE WAS %2\$s, AND THE CROWN THAT COUNTED WRONG PROVED IT.",
            evidenceNames = listOf(
                "THE LOCKED DOOR LIE",
                "THE RANK ROW ON THE CASE",
                "THE MISCUT CROWN",
                "THE KEY ROOM ADMISSION"
            )
        ),
        ChapterData(
            title = "THE SILENT BOX",
            premise = "BOX FIVE IS BOOKED UNDER A NAME NOBODY IN THE HOUSE EVER WROTE.",
            houseRecord = "BOX FIVE WAS PAID IN FULL BEFORE THE DOORS OPENED.",
            witnesses = listOf(
                WitnessSeed(
                    name = "BOX OFFICE CLERK",
                    time = "18:30",
                    truthClaim = "THE PAYMENT WAS ENTERED BEFORE I UNBARRED THE DOORS.",
                    lieClaim = "BOX FIVE WAS STILL UNPAID WHEN THE DOORS OPENED."
                ),
                WitnessSeed(
                    name = "THE USHER",
                    time = "19:00",
                    truthClaim = "I LED NO ONE UP TO BOX FIVE ALL EVENING.",
                    lieClaim = "I TOOK THE MONEY FOR BOX FIVE DURING THE SECOND ACT."
                ),
                WitnessSeed(
                    name = "THE DOOR WATCH",
                    time = "18:15",
                    truthClaim = "THE DOORS STAYED BARRED UNTIL THE LEDGER WAS CLOSED.",
                    lieClaim = "THE DOORS STOOD OPEN AN HOUR BEFORE ANY MONEY CAME IN."
                ),
                WitnessSeed(
                    name = "THE NIGHT CASHIER",
                    time = "22:40",
                    truthClaim = "THE BOX FIVE ENTRY WAS ALREADY SETTLED WHEN I COUNTED.",
                    lieClaim = "I SETTLED BOX FIVE MYSELF LONG AFTER THE DOORS OPENED."
                )
            ),
            cipherWords = listOf("LABEL", "FIELD", "ALIKE"),
            cipherDecoys = listOf("DECAL", "BEACH", "CLIFF", "BADGE"),
            genuineMark = "EVERY HOUSE BOOKING LABEL IS SEALED IN RUBY WAX AND NUMBERED.",
            genuineProps = listOf(
                "A BOOKING LABEL IN RUBY WAX, NUMBERED NINE.",
                "A LABEL WITH RUBY WAX AND THE NUMBER FOURTEEN.",
                "A RUBY WAX SEAL OVER A LABEL NUMBERED THREE.",
                "A NUMBERED LABEL CLOSED WITH RUBY WAX.",
                "A LABEL BEARING RUBY WAX AND THE NUMBER TWENTY.",
                "A RUBY SEALED LABEL CARRYING THE NUMBER SIX."
            ),
            forgedProps = listOf(
                "A LABEL IN RUBY WAX WITH NO NUMBER AT ALL.",
                "A NUMBERED LABEL CLOSED WITH PLAIN BROWN WAX.",
                "A LABEL WHOSE SEAL IS PRINTED, NOT PRESSED IN WAX."
            ),
            targetName = "BOX OFFICE CLERK",
            targetRole = "KEEPER OF THE BOOKINGS",
            pressQuestions = listOf(
                "WHOSE HAND WROTE THE NAME IN BOX FIVE?",
                "WHY IS THE FIVE OCLOCK PAGE TORN FROM YOUR LEDGER?"
            ),
            deflections = listOf(
                "DO MANY GUESTS ASK FOR THE UPPER BOXES?",
                "IS THE VELVET REPLACED EACH SEASON?",
                "HOW LATE DOES THE OFFICE STAY OPEN?"
            ),
            suspects = listOf(
                SuspectSeed("BOX OFFICE CLERK", "KEEPER OF THE BOOKINGS"),
                SuspectSeed("THE USHER", "GUIDE OF THE AISLES"),
                SuspectSeed("THE NIGHT CASHIER", "COUNTER OF THE TAKINGS"),
                SuspectSeed("THE IMPRESARIO", "OWNER OF THE HOUSE")
            ),
            relations = listOf(
                RelationSeed(0, 3, "ALIBI"),
                RelationSeed(0, 2, "DEBT"),
                RelationSeed(1, 2, "RIVAL"),
                RelationSeed(1, 3, "MENTOR")
            ),
            motives = listOf("THE BORROWED NAME", "THE HOUSE CUT", "THE OLD FAVOUR"),
            trueSuspect = 0,
            trueMotive = 0,
            revealTemplate = "%1\$s SOLD BOX FIVE UNDER A BORROWED NAME AND KEPT THE PAGE. THE MOTIVE WAS %2\$s, AND THE UNNUMBERED SEAL PROVED IT.",
            evidenceNames = listOf(
                "THE PAID IN FULL LIE",
                "THE RANK ROW ON THE STUB",
                "THE UNNUMBERED SEAL",
                "THE TORN PAGE ADMISSION"
            )
        ),
        ChapterData(
            title = "THE JOKER NOTE",
            premise = "A JOKER IS NAILED TO THE STAGE DOOR WITH A CIPHER ACROSS ITS FACE.",
            houseRecord = "THE JOKER WAS NAILED UP AFTER THE HOUSE STOOD EMPTY.",
            witnesses = listOf(
                WitnessSeed(
                    name = "STAGE DOOR WATCH",
                    time = "23:50",
                    truthClaim = "THE DOOR WAS BARE WHEN THE LAST PLAYER WENT HOME.",
                    lieClaim = "THE JOKER HUNG THERE WHILE THE CROWD WAS STILL INSIDE."
                ),
                WitnessSeed(
                    name = "THE LAMPLIGHTER",
                    time = "00:10",
                    truthClaim = "I SAW THE CARD WHEN I DOUSED THE LAST BRASS LAMP.",
                    lieClaim = "I WATCHED THE CARD GO UP DURING THE SECOND ACT."
                ),
                WitnessSeed(
                    name = "THE CLEANER",
                    time = "00:30",
                    truthClaim = "I SWEPT PAST THE CARD WHEN THE HOUSE WAS LONG EMPTY.",
                    lieClaim = "THE CARD WAS NAILED UP BEFORE THE FIRST GUEST LEFT."
                ),
                WitnessSeed(
                    name = "THE NIGHT PORTER",
                    time = "01:00",
                    truthClaim = "THE NAIL WAS STILL WARM WHEN I LOCKED THE YARD.",
                    lieClaim = "THAT CARD HAD HUNG ON THE DOOR SINCE THE OVERTURE."
                )
            ),
            cipherWords = listOf("FACED", "CAGED", "HEDGE"),
            cipherDecoys = listOf("FABLE", "LEDGE", "IDEAL", "CABLE"),
            genuineMark = "EVERY JOKER OF THE HOUSE WEARS A GOLD TEAR UNDER THE LEFT EYE.",
            genuineProps = listOf(
                "A JOKER WITH A GOLD TEAR BELOW THE LEFT EYE.",
                "A JOKER MASK PAINTED WITH A GOLD TEAR, LEFT SIDE.",
                "A JOKER CARD SHOWING THE GOLD TEAR ON THE LEFT.",
                "A JOKER WHOSE LEFT EYE CARRIES THE GOLD TEAR.",
                "A JOKER PRINTED WITH ONE GOLD TEAR, LEFT SIDE.",
                "A JOKER BEARING THE GOLD TEAR BENEATH THE LEFT EYE."
            ),
            forgedProps = listOf(
                "A JOKER WITH THE GOLD TEAR UNDER THE RIGHT EYE.",
                "A JOKER WHOSE TEAR IS PAINTED RUBY, NOT GOLD.",
                "A JOKER WITH NO TEAR BENEATH EITHER EYE."
            ),
            targetName = "THE IMPRESARIO",
            targetRole = "OWNER OF THE HOUSE",
            pressQuestions = listOf(
                "WHO SIGNED THE FOUR CASES INTO THE HOUSE LEDGER?",
                "WHY DOES EVERY CLOSED CASE END AT YOUR OWN DOOR?"
            ),
            deflections = listOf(
                "DO YOU STILL TAKE THE LATE SUPPER?",
                "HOW MANY SEASONS HAS THE HOUSE RUN?",
                "IS THE GOLD LEAF RENEWED EACH YEAR?"
            ),
            suspects = listOf(
                SuspectSeed("THE IMPRESARIO", "OWNER OF THE HOUSE"),
                SuspectSeed("THE UNDERSTUDY", "SECOND LEAD OF THE HOUSE"),
                SuspectSeed("THE CARD PRINTER", "KEEPER OF THE PRESS"),
                SuspectSeed("THE PROP MASTER", "KEEPER OF THE PROP ROOM")
            ),
            relations = listOf(
                RelationSeed(0, 1, "MENTOR"),
                RelationSeed(0, 2, "DEBT"),
                RelationSeed(0, 3, "RIVAL"),
                RelationSeed(1, 2, "ALIBI")
            ),
            motives = listOf("THE FOUR SIGNATURES", "THE CLOSING HOUSE", "THE LAST ENCORE"),
            trueSuspect = 0,
            trueMotive = 0,
            revealTemplate = "%1\$s PLANNED ALL FOUR CASES AND LEFT THE JOKER AS A SIGNATURE. THE MOTIVE WAS %2\$s, AND THE TEAR ON THE WRONG EYE PROVED IT.",
            evidenceNames = listOf(
                "THE EMPTY HOUSE LIE",
                "THE RANK ROW ON THE JOKER",
                "THE MISPLACED GOLD TEAR",
                "THE LEDGER ADMISSION"
            )
        )
    )
}
