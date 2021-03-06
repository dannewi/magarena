def TakeCard = {
    final MagicGame game, final MagicEvent event ->
    event.processChosenCards(game, {
        final MagicCard chosen ->
        final MagicCardList library = new MagicCardList(event.getPlayer().getLibrary().getCardsFromTop(4));
        for (final MagicCard card : library) {
            if (card == chosen) { // Not a draw action, card is 'put' into hand
                game.doAction(new MoveCardAction(card, MagicLocationType.OwnersLibrary, MagicLocationType.OwnersHand));
                game.doAction(new RemoveCardAction(card, MagicLocationType.OwnersLibrary));
            } else { // Not a discard action, card is 'put' into graveyard
                game.doAction(new MoveCardAction(card, MagicLocationType.OwnersLibrary, MagicLocationType.Graveyard));
                game.doAction(new RemoveCardAction(card, MagicLocationType.OwnersLibrary));
            }
        }
    });
}

[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack, final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "PN looks at the top four cards of his or her library. PN puts one of them into "+
                "his or her hand and the rest into his or her graveyard."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPlayer player = event.getPlayer();
            final List<MagicCard> topCards = player.getLibrary().getCardsFromTop(4);
            game.addEvent(new MagicEvent(
                event.getSource(),
                player,
                new MagicFromCardListChoice(topCards,1),
                MagicGraveyardTargetPicker.ReturnToHand,
                TakeCard,
                "PN puts a card into his or her hand."
            ));
        }
    }
]
