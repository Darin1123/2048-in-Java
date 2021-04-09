package model;

import view.Observer;

public abstract class Subject<T> {
    private Observer<T> observer;
    private T state;

    public void attach(Observer<T> observer) {
        this.observer = observer;
    }

    public T getState() {
        return this.state;
    }

    public void setState(T newState) {
        this.state = newState;
    }

    public void notifyObserver() {
        this.observer.update();
    }
}
