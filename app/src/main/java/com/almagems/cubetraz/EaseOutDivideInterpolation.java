package com.almagems.cubetraz;



public final class EaseOutDivideInterpolation {

    private float _value;
    private float _target;
    private float _divisor;
    private float _t;
    private float _d;
    private float  _from;

    private float linearTween(float t, float start, float end) {
        return t * start + (1 - t) * end;
    }

    public boolean setup(float from, float to, float divisor) {
        if (divisor <= 0) {
            return false;
        }

        _value = _from = from;
        _target = to;
        _divisor = divisor;
        _t = 0.0f;
        _d = 20.0f;

        return true;
    }

//	bool Interpolate()
//	{
//		_t += 1.0f;
//
//		float t = _t / _d;
//
//		_value = -_target * t * (t-2) + _from;
//
//
//
//		return false;
//	}


//	bool Interpolate()
//	{
//		_t -= 0.03f;
//		if (_t < 0.0f) _t = 0.0f;
//
//		_value = LinearTween(_t*_t, _from, _target);
//
//		return false;
//	}

    public boolean interpolate() {
        float oldValue = _value;

        if(_divisor > 0) {
            _value = ( _value * (_divisor - 1.0f) + _target ) / _divisor;
        }

        return (_value == oldValue);
    }

    public float getValue() {
        return _value;
    }

}
