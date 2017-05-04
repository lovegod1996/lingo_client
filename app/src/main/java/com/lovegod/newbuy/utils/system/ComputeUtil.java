package com.lovegod.newbuy.utils.system;

/**
 * 已知到三点距离，求坐标
 * Created by 123 on 2017/5/1.
 */

public class ComputeUtil {

    public static Point getTPoint(Point A, Point B, Point C, Double oa, Double ob, Double oc) {
        //先处理两个点，在处理另一个点
        //假设所求点为O
        Point O = new Point();
        double[] doubles1 = PointIntersect(A, B, oa, ob);
        double[] doubles2 = PointIntersect(B, C, ob, oc);
        O = pointAdpter(doubles1, doubles2);
        return O ;
    }

    /**
     * 根据两个点找出较为合适的
     *
     * @param doubles1
     * @param doubles2
     * @return
     */
    private static Point pointAdpter(double[] doubles1, double[] doubles2) {
        Point o1 = new Point();
        Point o2 = new Point();
        Point o3 = new Point();
        Point o4 = new Point();

        o1.setX(doubles1[0]);
        o1.setY(doubles1[1]);
        o2.setX(doubles1[2]);
        o2.setY(doubles1[3]);
        o3.setX(doubles2[0]);
        o3.setY(doubles2[1]);
        o4.setX(doubles2[2]);
        o4.setY(doubles2[3]);
        //计算四点间的距离，找到最小值
        Double[] doubles = new Double[4];
        doubles[0]=length(o1,o3);
        doubles[1]=length(o1,o4);
        doubles[2]=length(o2,o3);
        doubles[3]=length(o2,o4);

        double min=doubles[0];
        //找出最小值
        Point o=new Point();

        for(int i=0;i<doubles.length;i++){
            if(doubles[i]<min){
                min=doubles[i];
            }
        }


        for(int j=0;j<doubles.length;j++){
            if(doubles[j]==min){
                switch (j){
                    case 0:
                        double x=(o1.getX()+o3.getX())/2;
                        double y=(o1.getY()+o3.getY())/2;
                        o.setX(x);
                        o.setY(y);
                        break;
                    case 1:
                        double x1=(o1.getX()+o4.getX())/2;
                        double y1=(o1.getY()+o4.getY())/2;
                        o.setX(x1);
                        o.setY(y1);
                        break;
                    case 2:
                        double x2=(o2.getX()+o3.getX())/2;
                        double y2=(o2.getY()+o3.getY())/2;
                        o.setX(x2);
                        o.setY(y2);
                        break;
                    case 3:
                        double x3=(o2.getX()+o4.getX())/2;
                        double y3=(o2.getY()+o4.getY())/2;
                        o.setX(x3);
                        o.setY(y3);
                        break;
                    default:
                        break;
                }
            }
        }
        return o;
    }

    /**
     * 根据两点的坐标和距离计算交点
     *
     * @param AA
     * @param BB
     * @param oa
     * @param ob
     * @return {x1 , y1 , x2 , y2}
     */
    private static double[] PointIntersect(Point AA, Point BB, Double oa, Double ob) {
        double x1 = AA.getX();
        double y1 = AA.getY();
        double x2 = BB.getX();
        double y2 = BB.getY();
        double r1 = oa;
        double r2 = ob;

        // 在一元二次方程中 a*x^2+b*x+c=0
        double a, b, c;

        //x的两个根 x_1 , x_2
        //y的两个根 y_1 , y_2
        double x_1 = 0, x_2 = 0, y_1 = 0, y_2 = 0;

        //判别式的值
        double delta = -1;

        //如果 y1!=y2
        if (y1 != y2) {

            //为了方便代入
            double A = (x1 * x1 - x2 * x2 + y1 * y1 - y2 * y2 + r2 * r2 - r1 * r1) / (2 * (y1 - y2));
            double B = (x1 - x2) / (y1 - y2);

            a = 1 + B * B;
            b = -2 * (x1 + (A - y1) * B);
            c = x1 * x1 + (A - y1) * (A - y1) - r1 * r1;

            //下面使用判定式 判断是否有解
            delta = b * b - 4 * a * c;

            if (delta > 0) {

                x_1 = (-b + Math.sqrt(b * b - 4 * a * c)) / (2 * a);
                x_2 = (-b - Math.sqrt(b * b - 4 * a * c)) / (2 * a);
                y_1 = A - B * x_1;
                y_2 = A - B * x_2;
            } else if (delta == 0) {
                x_1 = x_2 = -b / (2 * a);
                y_1 = y_2 = A - B * x_1;
            } else {
                System.err.println("两个圆不相交");
                return null;
            }
        } else if (x1 != x2) {

            //当y1=y2时，x的两个解相等
            x_1 = x_2 = (x1 * x1 - x2 * x2 + r2 * r2 - r1 * r1) / (2 * (x1 - x2));

            a = 1;
            b = -2 * y1;
            c = y1 * y1 - r1 * r1 + (x_1 - x1) * (x_1 - x1);

            delta = b * b - 4 * a * c;

            if (delta > 0) {
                y_1 = (-b + Math.sqrt(b * b - 4 * a * c)) / (2 * a);
                y_2 = (-b - Math.sqrt(b * b - 4 * a * c)) / (2 * a);
            } else if (delta == 0) {
                y_1 = y_2 = -b / (2 * a);
            } else {
                System.err.println("两个圆不相交");
                return null;
            }
        } else {
            System.out.println("无解");
            return null;
        }
        return new double[]{x_1, y_1, x_2, y_2};
    }

    private static double length(Point a, Point b) {
        return Math.sqrt(Math.pow(a.getX() - b.getX(), 2) + Math.pow(a.getY() - b.getY(), 2));
    }

}
