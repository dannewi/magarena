[
    new MagicWhenDiesTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPermanent died) {
            return new MagicEvent(
                permanent,
                this,
                "Exile SN and each other player (${permanent.getController().getOpponent().getName()}) "+
                "puts a 3/3 black Angel creature token with flying onto the battlefield."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicCard card = event.getPermanent().getCard();
            game.doAction(new RemoveCardAction(card, MagicLocationType.Graveyard));
            game.doAction(new MoveCardAction(card, MagicLocationType.Graveyard, MagicLocationType.Exile));
            game.doAction(new PlayTokenAction(
                event.getPlayer().getOpponent(),
                CardDefinitions.getToken("3/3 black Angel creature token with flying")
            ))
        }
    }
]
