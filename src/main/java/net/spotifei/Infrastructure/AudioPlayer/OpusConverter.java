package net.spotifei.Infrastructure.AudioPlayer;

import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;

import javax.sound.sampled.*;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.ShortBuffer;

public final class OpusConverter {

    /**
     * Porque isso existe? Porque o Java nativo não suporta envio de
     * arquivos .opus em AudioInputStream, e os arquivos .opus são
     * os arquivos de aúdios mais leves que existem, bom para guardar
     * e receber do banco, portanto, somos forçados a criar um hardcode
     * para converter esse tipo de arquivo para usar no projeto...
     * @param opusBytes Bytes do arquivo .opus
     * @return AudioInputStream gerado pelos bytes
     * @throws Exception Gera uma excessão se ocorrer qualquer erro na conversão
     */
    public static AudioInputStream convertOpusBytesToAudioInputStream(byte[] opusBytes) throws Exception {

        // obtem o transformador magico do ffmpeg
        try (FFmpegFrameGrabber grabber =
                     new FFmpegFrameGrabber(new ByteArrayInputStream(opusBytes));
             ByteArrayOutputStream pcmOut = new ByteArrayOutputStream()) {

            grabber.setFormat("ogg"); // seta o formato do arquivo de .opus para .ogg (facilitar conversão)
            grabber.start(); // inicia a tarefa de transformar em ogg

            // o principal objetivo disso é obter os bytes em .ogg
            // para depois transformar em .wav escrevendo byte por byte,
            // já que o java não nos dá opção a não ser usar .wav

            int sampleRate = grabber.getSampleRate();
            int channels   = grabber.getAudioChannels();
            if (sampleRate <= 0 || channels <= 0) throw new UnsupportedAudioFileException("Formato de aúdio não suportado!");

            Frame frame; // Classe que basicamente Gerencia os dados do aúdio

            // Os samples representam parcelas do aúdio em forma de Buffer
            // O loop tenta obter todos os samples do aúdio para gerar o
            // arquivo de aúdio resultante do 0 basicamente
            // loopa até não ter mais samples
            while ((frame = grabber.grabSamples()) != null) {
                // Escreve as samples nos bytes de output do PCM
                // Buffer -> Stream
                writeBufferOnStream((ShortBuffer) frame.samples[0], pcmOut);
            }

            // Re-formata o aúdio .opus para ficar como um ".wav" que é o aceito pelo AudioInputStream do Java
            // por favor Java coloca suporte a .opus :pray:
            AudioFormat fmt = new AudioFormat(sampleRate, 16, channels, true, false);


            return new AudioInputStream(new ByteArrayInputStream(pcmOut.toByteArray()),
                    fmt,
                    pcmOut.toByteArray().length / fmt.getFrameSize());
        }
    }

    /**
     * Basicamente preenche uma Stream com as samples de um buffer
     * @param src buffer de input
     * @param out Stream de output
     * @throws IOException Gera uma excessão caso não seja possível acessar os bytes
     */
    private static void writeBufferOnStream(ShortBuffer src, OutputStream out) throws IOException {
        int nSamples = src.remaining(); // retorna o delta da posicao das samples
        // preparação para receber sample
        ByteBuffer bb = ByteBuffer
                .allocate(nSamples * 2) // aloca a quantidade de bytes necessária para a sample
                .order(ByteOrder.LITTLE_ENDIAN); // formata os bytes de maior para menor

        bb.asShortBuffer().put(src); // aloca a sample no buffer
        out.write(bb.array(), 0, nSamples * 2); // escreve sample no stream de output
    }
}