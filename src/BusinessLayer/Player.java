package BusinessLayer;

abstract public class Player extends Unit{

    //fields

    protected Integer level;
    protected Integer experience;
    protected final String abilityName;

    //constructors

    public Player(String name, Integer healthPool, Integer attackPoints, Integer defensePoints, String abilityName) {
        super('@', name, healthPool, attackPoints, defensePoints);
        this.abilityName = abilityName;
        level = 1;
        experience = 0;
    }

    //methods

    protected void addExperience(Integer value){
        experience += value;
        if (experience >= level*50)
            lvlup();
    }

    protected void lvlup() {
        experience -= 50 * level;
        level++;
        health.increasePool(10 * level);
        increaseAtt(4 * level);
        increaseDef(1 * level);
    }

    public String description(){
        return super.description() + "\tLevel: " + level + "\tExperience: " + experience + '/' + (level * 50);
    }

    public void onDeath() {
        setTileChar('X');
        deathCallback.call();
    }

    public void onKill(Enemy e){
        addExperience(e.getExperienceValue());
    }
}
