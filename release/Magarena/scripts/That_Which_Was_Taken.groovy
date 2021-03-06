def EXCEPT_SELF = new MagicPermanentFilterImpl() {
    public boolean accept(final MagicSource source, final MagicPlayer player, final MagicPermanent target) {
        return target != source && target.isController(player);
    }
};

def TARGET_PERMANENT_EXCEPT = {
    final MagicSource source ->
    return new MagicTargetChoice(
        EXCEPT_SELF,
        MagicTargetHint.Positive,
        "a permanent except ${source.getName()}"
    );
};
[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Pump),
        "+Counter"
    ) {

        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicTapEvent(source),
                new MagicPayManaCostEvent(source,"{4}")
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                TARGET_PERMANENT_EXCEPT(source),
                MagicIndestructibleTargetPicker.create(),
                this,
                "PN puts a divinity counter on target permanent other than SN.\$"
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                game.doAction(new ChangeCountersAction(it, MagicCounterType.Divinity, 1));
            })
        }
    },
    
    new MagicStatic(MagicLayer.Ability, PERMANENT) {
        @Override
        public void modAbilityFlags(final MagicPermanent source, final MagicPermanent permanent, final Set<MagicAbility> flags) {
            permanent.addAbility(MagicAbility.Indestructible, flags);
        }
        @Override
        public boolean condition(final MagicGame game, final MagicPermanent source, final MagicPermanent target) {
            return target.hasCounters(MagicCounterType.Divinity);
        }
    },
]
