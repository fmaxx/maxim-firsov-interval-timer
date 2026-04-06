package run.simple.feature_training_screen.data

class TimeModel {

    var currentMs: Long = 0

    fun increment(passed: Long) {
        currentMs += passed
    }
}