package mafia.entities;

import java.util.Random;

/**
 * 2017-02-19.
 */
public class Story {

    private String name;

    //String for the story
    private String story;

    //String that stores either the heal or death of a player depending on what was called from Game
    private String event;

    //constructor method
    public Story(String str) {
        name = str;
    }

    //randomizes an initial story to print out
    public void initialScenario() {
        String story1 = "Last night " + name + " visited " + location() + ". They were on their way to meet their friend Jim. They were about to take their usual route when they saw that it had been blocked off due to a group of people having a dance contest, so they decided to take another route. The only way around the crowd that they knew of was down a dark alley around the corner from their house. They grabbed their belongings and they set off. On their journey, they felt as if they were being watched. Assuming they were being paranoid they decided to keep walking. Suddenly the craziest thing happened";
        String story2 = "On any average day anyone could go out into " + location() + " without worrying about getting hurt. On this fateful night, " + name + " wasn't so lucky.";
        String story3 = "Last night there was a huge party down at " + location() + " and " + name + " was invited. They didn't really like the host all that much but decided to attend anyway. As the party was in full swing, They decided to take a walk and get some fresh air. Getting away from all the noise and into the dark night would really do them some good. When, suddenly, they were shoved.";
        String story4 = "Last night, " + name + "was in " + location() + " . It was a night like any other night.";
        String story5 = name + " was casually enjoying their day. They decided to make their way to " + location() + " when suddenly, something crazy happened. They were attacked!";
        String story6 = "Jim told his friend " + name + " that he would meet him at " + location() + ". It was just like any other night. " + name + " was patiently waiting when all of a sudden they were attacked!";
        String story7 = name + " was enjoying their day at " + location() + " when it quickly turned sour. He was attacked by a mysterious figure!";
        String story8 = "On this fateful night " + name + " was on their way to " + location() + " when they felt something leering behind them. Before they could check they were being pinned to the ground by an attacker!";
        String story9 = name + " made it to " + location() + " at around 9:30, like he does every night. This night felt like very other night but what happened next would cause them to change their mind. All of a sudden they were roughly shoved to the ground.";
        String story10 = name + " went to " + location() + " last night. They were attacked!";

        String [] stories = {story1, story2, story3, story4, story5, story6, story7, story8, story9, story10};

        int randomNumber = new Random().nextInt(10);
        story = stories[randomNumber];
    }
    //randomizes and returns location for initialScenario()
    public String location() {
        String location1 = "the alley";
        String location2 = "the lake";
        String location3 = "the restaurant";
        String location4 = "the store downtown";
        String location5 = "the city park";
        String location6 = "the bus station";
        String location7 = "the dock";
        String location8 = "the underground parking garage";
        String location9 = "the graveyard";
        String location10 = "the motel";

        String [] locations = {location1, location2, location3, location4, location5, location6, location7, location8, location9, location10};

        int randomNumber = new Random().nextInt(10);
        String answer = locations[randomNumber];
        return answer;
    }
    //randomizes and returns causes of death for dead() and healed()
    public String causeOfDeath() {
        String death1 = "stabbed";
        String death2 = "shot";
        String death3 = "poisoned";
        String death4 = "choked";
        String death5 = "strangled";
        String death6 = "beat up";
        String death7 = "stoned";
        String death8 = "mutilated";
        String death9 = "suffocated";
        String death10 = "kidnapped";

        String [] deaths = {death1, death2, death3, death4, death5, death6, death7, death8, death9, death10};

        int randomNumber = new Random().nextInt(10);
        String answer = deaths[randomNumber];
        return answer;
    }
    //prints out how the player was killed by the assassin
    public void dead() {
        event = "The assasin " + causeOfDeath() + " " + name + ". Rest in peace.";
    }
    //prints out how the player was saved by the doctor
    public void healed() {
        event = "The doctor saved " + name + " after they were " + causeOfDeath();
    }
    public String printStory(){
        return story;
    }

    //returns both the dead and alive Strings, depeding on what was called in Game
    public String getEvent(){
        return event;
    }

}
