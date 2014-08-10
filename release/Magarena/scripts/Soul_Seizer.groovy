[
    new MagicWhenSelfCombatDamagePlayerTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            return new MagicEvent(
                permanent,
                new MagicMayChoice(MagicTargetChoice.TARGET_CREATURE_YOUR_OPPONENT_CONTROLS),
                damage.getTarget(),
                this,
                "PN may\$ transform SN and attach it to target creature\$ RN controls."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                event.processTargetPermanent(game, {
                    game.doAction(new MagicTransformAction(event.getPermanent()));
                    game.doAction(new MagicAttachAction(event.getPermanent(), it));
                });
            }
        }
    }
]
