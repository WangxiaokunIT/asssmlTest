import { Circle as VCCircle } from '../vc-progress';
import { validProgress } from './utils';

var statusColorMap = {
  normal: '#108ee9',
  exception: '#ff5500',
  success: '#87d068'
};

function getPercentage(_ref) {
  var percent = _ref.percent,
      successPercent = _ref.successPercent;

  var ptg = validProgress(percent);
  if (!successPercent) return ptg;

  var successPtg = validProgress(successPercent);
  return [successPercent, validProgress(ptg - successPtg)];
}

function getStrokeColor(_ref2) {
  var progressStatus = _ref2.progressStatus,
      successPercent = _ref2.successPercent,
      strokeColor = _ref2.strokeColor;

  var color = strokeColor || statusColorMap[progressStatus];
  if (!successPercent) return color;
  return [statusColorMap.success, color];
}

var Circle = {
  functional: true,
  render: function render(h, context) {
    var props = context.props,
        children = context.children;
    var prefixCls = props.prefixCls,
        width = props.width,
        strokeWidth = props.strokeWidth,
        trailColor = props.trailColor,
        strokeLinecap = props.strokeLinecap,
        gapPosition = props.gapPosition,
        gapDegree = props.gapDegree,
        type = props.type;

    var circleSize = width || 120;
    var circleStyle = {
      width: typeof circleSize === 'number' ? circleSize + 'px' : circleSize,
      height: typeof circleSize === 'number' ? circleSize + 'px' : circleSize,
      fontSize: circleSize * 0.15 + 6
    };
    var circleWidth = strokeWidth || 6;
    var gapPos = gapPosition || type === 'dashboard' && 'bottom' || 'top';
    var gapDeg = gapDegree || type === 'dashboard' && 75;

    return h(
      'div',
      { 'class': prefixCls + '-inner', style: circleStyle },
      [h(VCCircle, {
        attrs: {
          percent: getPercentage(props),
          strokeWidth: circleWidth,
          trailWidth: circleWidth,
          strokeColor: getStrokeColor(props),
          strokeLinecap: strokeLinecap,
          trailColor: trailColor,
          prefixCls: prefixCls,
          gapDegree: gapDeg,
          gapPosition: gapPos
        }
      }), children]
    );
  }
};

export default Circle;