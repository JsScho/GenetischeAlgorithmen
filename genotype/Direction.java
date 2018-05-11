/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package genotype;

/**
 *
 * @author Skurrrman
 */
    public enum Direction {
        LEFT,UP,RIGHT,DOWN;
        
        public Direction getRelativeLeft(){
            return values()[ mod(ordinal()-1,Direction.values().length)];
        }
        public Direction getRelativeRight(){
            return values()[ mod(ordinal()+1,Direction.values().length)];
        }
        private int mod(int a, int b)
        {
            int ret = a % b;
            if (ret < 0)
                ret += b;
            return ret;
        }
    }
   
