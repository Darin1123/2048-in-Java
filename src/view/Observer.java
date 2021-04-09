package view;

import model.Subject;

/**
 * Abstract <code>Observer</code> class which is a part of the
 * observer design pattern.
 * @author Zefeng Wang
 */

public abstract class Observer<T> {
    protected final Subject<T> subject;

    public Observer(Subject<T> subject) {
        this.subject = subject;
        subject.attach(this);
    }

    public abstract void update();
}
