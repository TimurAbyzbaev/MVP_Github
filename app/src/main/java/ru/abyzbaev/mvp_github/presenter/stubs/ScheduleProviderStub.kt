package ru.abyzbaev.mvp_github.presenter.stubs

import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers
import ru.abyzbaev.mvp_github.rx.SchedulerProvider

class ScheduleProviderStub : SchedulerProvider {
    override fun ui(): Scheduler {
        return Schedulers.trampoline()
    }

    override fun io(): Scheduler {
        return Schedulers.trampoline()
    }

}