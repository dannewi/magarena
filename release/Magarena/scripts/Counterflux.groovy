[
    new MagicOverloadActivation(MagicTiming.Counter) {
        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicCard source) {
            return [
                new MagicPayManaCostEvent(source,"{1}{U}{U}{R}")
            ];
        }
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "Counter each spell you don't control."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            SPELL_YOU_DONT_CONTROL.filter(event) each {
                game.doAction(new CounterItemOnStackAction(it));
            }
        }
    }
]
