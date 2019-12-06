package app.raybritton.smartmirror.data.monitors

import app.raybritton.elog.ELog
import app.raybritton.smartmirror.BuildConfig
import app.raybritton.smartmirror.data.api.FirewallService
import app.raybritton.smartmirror.data.api.IpService
import app.raybritton.smartmirror.data.templates.FirewallTemplate
import app.raybritton.smartmirror.data.templates.Rule
import app.raybritton.smartmirror.data.templates.Sources
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.subjects.ReplaySubject
import timber.log.Timber
import java.util.concurrent.TimeUnit

class IpAddressMonitor(
    private val firewallService: FirewallService,
    private val ipService: IpService
) {
    private val hasDied = ReplaySubject.createWithSize<Boolean>(1).apply { startWith(false) }

    fun monitor() {
        Flowable.interval(0, 30, TimeUnit.MINUTES)
            .flatMap {
                ipService.getIpAddress()
                    .map { it.ip }
                    .toFlowable()
            }
            .distinctUntilChanged()
            .flatMap { ip ->
                val rules = listOf(
                    Rule(ports = "22", sources = Sources(listOf(*BuildConfig.FIREWALL_FIXED_IPS + ip)))
                )
                val template = FirewallTemplate(inbound_rules = rules)
                firewallService.updateFirewall(BuildConfig.FIREWALL_ID, BuildConfig.FIREWALL_TOKEN, template)
                    .andThen(Flowable.just(true))
            }
            .subscribe({

            },{
                Timber.e(it, "ip monitor")
                ELog.submitCurrentLogSilently("ip monitor", false)
                hasDied.onNext(true)
            },{
                Timber.e("ip monitor ended prematurely")
                ELog.submitCurrentLogSilently("ip monitor", false)
                hasDied.onNext(true)
            })
    }

    fun watchSystemFailure(): Flowable<Boolean> {
        return hasDied.share().toFlowable(BackpressureStrategy.LATEST)
    }
}