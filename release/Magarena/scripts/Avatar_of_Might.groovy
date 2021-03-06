[
    new MagicHandCastActivation(
        [MagicCondition.CARD_CONDITION],
        new MagicActivationHints(MagicTiming.Main, true),
        "Cast"
    ) {
        @Override
        public void change(final MagicCardDefinition cdef) {
            cdef.setHandAct(this);
        }

        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicCard source) {
            return source.getOpponent().getNrOfPermanents(MagicType.Creature) >= source.getController().getNrOfPermanents(MagicType.Creature) + 4 ?
                [new MagicPayManaCostEvent(source,"{G}{G}")] :
                [new MagicPayManaCostEvent(source,"{6}{G}{G}")];
        }
    }
]
