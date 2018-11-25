/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package  com.ralab.pos;

/**
 *
 * @author admin
 */
public class SceneTypeClass {
    private static SceneType scene_type = SceneType.NONE;
    public static void setSceneType(SceneType st){
        scene_type = st;
    }
    public static SceneType getSceneType(){
        return scene_type;
    }
    
}
