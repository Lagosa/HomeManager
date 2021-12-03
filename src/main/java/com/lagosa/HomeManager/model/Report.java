package com.lagosa.HomeManager.model;

import java.util.List;

public class Report {
    private final User user;
    private final List<Chore> choresDone;
    private final List<Chore> choresTookUpAndNotDone;
    private final int nrOfDoneChores;
    private final int nrOfNotFinishedChores;

    public Report(User user, List<Chore> choresDone, List<Chore> choresTookUp, int nrOfDoneChores, int nrOfNotFinishedChores) {
        this.user = user;
        this.choresDone = choresDone;
        this.choresTookUpAndNotDone = choresTookUp;
        this.nrOfDoneChores = nrOfDoneChores;
        this.nrOfNotFinishedChores = nrOfNotFinishedChores;
    }

    public User getUser() {
        return user;
    }

    public List<Chore> getChoresDone() {
        return choresDone;
    }

    public List<Chore> getChoresTookUpAndNotDone() {
        return choresTookUpAndNotDone;
    }
    public int getNrOfDoneChores() {
        return nrOfDoneChores;
    }

    public int getNrOfNotFinishedChores() {
        return nrOfNotFinishedChores;
    }
}
