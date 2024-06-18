using UnityEngine;
using System.Collections;
using System.Collections.Generic;

public class AIScript_IsaiahLipford : MonoBehaviour {

    public CharacterScript mainScript;
	
	public float[] bombSpeed;
	public float playerSpeed;
	public float[] buttonCooldowns;
	public float[] buttonLocations;
	public float playerLocation, enemyLocation;
	public int[] beltDirections = new int[8];
	public float[] bombDistance = new float[8];

    void Start() {
        mainScript = GetComponent<CharacterScript>();

        if (mainScript == null) {
            print("No CharacterScript found on " + gameObject.name);
            this.enabled = false;
        }

        buttonLocations = mainScript.getButtonLocations();
        playerSpeed = mainScript.getPlayerSpeed();
    }

    int targetBelt = 0;
    public float CalculateBombTime(int t) {
        if (t < 0 || t > beltDirections.Length - 1)
            return Mathf.Infinity;
        return bombDistance[targetBelt] / bombSpeed[targetBelt];
    }

    void Update() {
        UpdateCharacterData();

        int closestButton = FindClosestButton();
        List<int> potentialTargets = FindPotentialTargets();

        targetBelt = SelectBestTarget(potentialTargets);

        MoveCharacter(targetBelt, closestButton);

        if (CanPushButton(targetBelt, closestButton)) {
            mainScript.push();
        }
    }

    void UpdateCharacterData() {
        buttonCooldowns = mainScript.getButtonCooldowns();
        beltDirections = mainScript.getBeltDirections();
        bombSpeed = mainScript.getBombSpeeds();
        playerLocation = mainScript.getCharacterLocation();
        enemyLocation = mainScript.getOpponentLocation();
        bombDistance = mainScript.getBombDistances();
        playerSpeed = mainScript.getPlayerSpeed();
    }

    int FindClosestButton() {
        int closestButton = 0;
        float closestDistance = Mathf.Infinity;

        for (int i = 0; i < beltDirections.Length; i++) {
            float distance = Mathf.Abs(playerLocation - buttonLocations[i]);
            if (distance < closestDistance) {
                closestButton = i;
                closestDistance = distance;
            }
        }
        return closestButton;
    }

    List<int> FindPotentialTargets() {
        List<int> potentialTargets = new List<int>();

        for (int i = 0; i < beltDirections.Length; i++) {
            if (beltDirections[i] == -1 || beltDirections[i] == 0) {
                float bombTime = bombDistance[i] / bombSpeed[i];
                float playerTime = Mathf.Abs(playerLocation - buttonLocations[i]) / playerSpeed;

                if (playerTime < bombTime && bombTime > buttonCooldowns[i]) {
                    potentialTargets.Add(i);
                }
            }
        }
        return potentialTargets;
    }

    int SelectBestTarget(List<int> potentialTargets) {
        float shortestTime = Mathf.Infinity;
        int bestTarget = 0;

        foreach (int target in potentialTargets) {
            float bombTime = bombDistance[target] / bombSpeed[target];

            if (shortestTime > bombTime) {
                shortestTime = bombTime;
                bestTarget = target;
            }
        }
        return bestTarget;
    }

    void MoveCharacter(int target, int closestButton) {
        if (buttonLocations[target] < playerLocation) {
            mainScript.moveDown();
        } else if (buttonLocations[target] > playerLocation) {
            mainScript.moveUp();
        }
    }

    bool CanPushButton(int target, int closestButton) {
        bool canReachInTime = (Mathf.Abs(playerLocation - buttonLocations[target]) / playerSpeed) + 0.3f < bombDistance[target] / bombSpeed[target];
        bool isAtTarget = target == closestButton;

        return (beltDirections[closestButton] != 1) && (canReachInTime || isAtTarget);
    }
}