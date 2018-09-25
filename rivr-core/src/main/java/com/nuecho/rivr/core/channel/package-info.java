/**
 * The dialogue channel and turns, some abstract concepts of Rivr.
 * <p>
 * The {@link com.nuecho.rivr.core.channel.DialogueChannel} is the interface
 * between the dialogue and the controller (e.g. servlet, unit test). The
 * dialogue sends an {@link com.nuecho.rivr.core.channel.OutputTurn} to the
 * {@link com.nuecho.rivr.core.channel.DialogueChannel} and gets an
 * {@link com.nuecho.rivr.core.channel.InputTurn} in return. The dialogue starts
 * with a {@link com.nuecho.rivr.core.channel.FirstTurn} and must terminate with
 * a {@link com.nuecho.rivr.core.channel.LastTurn}
 * <p>
 * The {@link com.nuecho.rivr.core.channel.DialogueChannel} is wrapped in the
 * {@link com.nuecho.rivr.core.dialogue.DialogueContext}
 * <p>
 * How a dialogue is executed:
 * <ol>
 * <li>The {@link com.nuecho.rivr.core.dialogue.Dialogue#run
 * dialogue.run(FirstTurn firstTurn, DialogueContext context)} method is called
 * by the controller.</li>
 * <li>The dialogue emits an {@link com.nuecho.rivr.core.channel.OutputTurn} by
 * invoking <code>dialogueChannel.doTurn(OutputTurn outputTurn)</code></li>
 * <li>It receives an {@link com.nuecho.rivr.core.channel.InputTurn} and use it
 * to progress in the logic.</li>
 * <li>The last two steps are repeated as required</li>
 * <li>The dialogue terminates by returning a
 * {@link com.nuecho.rivr.core.channel.LastTurn}
 * </ol>
 */

package com.nuecho.rivr.core.channel;

