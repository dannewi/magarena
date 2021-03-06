[
    new MagicWhenOtherSpellIsCastTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicCardOnStack cardOnStack) {
            return cardOnStack.hasType(MagicType.Enchantment) ?
                new MagicEvent(
                    permanent, 
                    cardOnStack,
                    this,
                    "Counter RN."
                ):
                MagicEvent.NONE; 
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) { 
            game.doAction(new CounterItemOnStackAction(event.getRefCardOnStack()));
        }
    }
]
