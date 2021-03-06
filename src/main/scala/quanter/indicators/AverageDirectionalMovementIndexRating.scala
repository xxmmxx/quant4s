/**
  *
  */
package quanter.indicators
import quanter.data.market.TradeBar
import quanter.indicators.window.RollingWindow

/**
  *
  */
class AverageDirectionalMovementIndexRating(pname: String, pperiod: Int) extends TradeBarIndicator(pname){
  def this(period: Int) {
    this("ADXR" + period, period)
  }

  private val _adx = new AverageDirectionalIndex(pname + "_ADX", pperiod)
  private val _adxHistory = new RollingWindow[Double](pperiod)
  private val _period = pperiod

  override def isReady: Boolean = samples >= _period
  override def reset = {
    _adx.reset
    _adxHistory.reset
    super.reset
  }
  override def computeNextValue(input: TradeBar): Double = {
    _adx.update(input)
    _adxHistory.add(_adx.current.value)

    (_adx + _adxHistory.get(math.min(_adxHistory.count - 1, _period - 1))) / 2
  }
}
