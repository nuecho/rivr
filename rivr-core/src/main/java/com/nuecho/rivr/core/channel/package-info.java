/**
 * The dialogue channel and turns, some abstract concepts of Rivr.
 * <p>
 * The <code>DialogueChannel</code> is the interface between the dialogue and
 * the controller (e.g. servlet, unit test). The dialogue sends
 * an <code>OutputTurn</code> to the <code>DialogueChannel</code> and gets an
 * <code>InputTurn</code> in return. The dialogue starts with a
 * <code>FirstTurn</code> and must terminate with a <code>LastTurn</code>
 * <p>The <code>DialogueChannel</code> is wrapped in the
 * <code>DialogueContext</code>
 * <p>How a dialogue is executed:
 * <p>
 * <ol>
 * <li>The <code>dialogue.run(FirstTurn firstTurn, DialogueContext
 * context)</code> method is called by the controller.</li>
 * <li>The dialogue emits an <code>OutputTurn</code> by invoking
 * <code>dialogueChannel.doTurn(OutputTurn outputTurn)</code></li>
 * <li>It receives an <code>InputTurn</code> and use it to progress in the
 * logic.</li>
 * <li>The last two steps are repeated as required</li>
 * <li>The dialogue terminates by returning a <code>LastTurn</code>
 * </ol>
 */

package com.nuecho.rivr.core.channel;

