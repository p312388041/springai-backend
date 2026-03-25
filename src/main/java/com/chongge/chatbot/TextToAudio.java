// package com.chongge.chatbot;
// import java.io.IOException;
// import org.springframework.ai.audio.tts.TextToSpeechModel;
// import org.springframework.ai.openai.OpenAiAudioSpeechModel;
// import org.springframework.ai.openai.OpenAiAudioSpeechOptions;
// import org.springframework.ai.openai.api.OpenAiApi.ChatCompletionRequest.AudioParameters.Voice;
// import org.springframework.ai.openai.api.OpenAiAudioApi;
// import org.springframework.ai.openai.api.OpenAiAudioApi.SpeechRequest.AudioResponseFormat;
// import org.springframework.core.io.FileSystemResource;
// import org.springframework.http.client.SimpleClientHttpRequestFactory;
// import org.springframework.web.client.RestClient;
// public class TextToAudio {
//   public static void main(String[] args) throws IOException, InterruptedException {
//     SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
//     factory.setConnectTimeout(600000); // 连接超时 10分钟
//     factory.setReadTimeout(600000); // 读取超时 10分钟
//     var builder = RestClient.builder().requestFactory(factory);
//     String baseUrl = "http://localhost:8021";
//     OpenAiAudioApi api = OpenAiAudioApi.builder().baseUrl(baseUrl).apiKey("hehe").restClientBuilder(builder).build();
//     var option = OpenAiAudioSpeechOptions.builder().model("tts-1").voice(Voice.ALLOY.name()).speed(1.0).responseFormat(AudioResponseFormat.AAC).build();
//     TextToSpeechModel model = new OpenAiAudioSpeechModel(api, option);
//     // TextToSpeechModel model = new OpenAiAudioSpeechModel(api, option);
//     String filePah = "d:/content2mp3.mp3";
//     String content = """
//       In the vast landscape of human achievement, few forces have shaped our world as profoundly as the relentless pursuit of innovation. From the earliest stone tools crafted by our ancestors to the sophisticated artificial intelligence systems of today, each breakthrough has built upon the foundations laid by those who came before. This journey of discovery is not merely a chronicle of technological advancement; it is a testament to the enduring human spirit that refuses to accept the status quo and constantly seeks better ways to live, work, and connect with one another.
//       Consider the Industrial Revolution of the eighteenth century, a period that fundamentally transformed how societies functioned. The invention of the steam engine by James Watt did not happen in isolation. It was the result of decades of experimentation, failure, and incremental improvements. Each setback provided valuable lessons that ultimately led to success. This pattern repeats throughout history. Thomas Edison famously conducted thousands of experiments before perfecting the practical light bulb. When asked about his failures, he remarked that he had not failed but had instead discovered ten thousand ways that would not work. This perspective on failure as a stepping stone rather than an obstacle remains profoundly relevant today.
//       In our modern era, the pace of innovation has accelerated exponentially. The development of the internet in the late twentieth century created a paradigm shift unlike anything previously witnessed. Suddenly, information could travel across the globe in seconds. Geographic barriers that had defined human interaction for millennia began to dissolve. The World Wide Web, conceived by Tim Berners-Lee at CERN, democratized access to knowledge in ways that were previously unimaginable. Today, a student in a remote village can access the same educational resources as someone at a prestigious university. This connectivity has spawned entirely new industries, transformed existing ones, and given rise to a global digital economy that continues to evolve at breathtaking speed.
//       Artificial intelligence represents the next frontier in this ongoing journey. Machine learning algorithms now power everything from medical diagnostics to autonomous vehicles. Large language models can engage in conversations, write creative content, and assist with complex problem-solving tasks. While concerns about the ethical implications of AI are legitimate and necessary, the potential benefits are equally significant. Imagine a world where personalized education adapts to each student's learning style, where medical breakthroughs occur at accelerated rates due to AI-assisted research, and where language barriers no longer hinder communication between people from different cultures.
//       The field of robotics has also made remarkable strides. Collaborative robots, or cobots, now work alongside humans in manufacturing environments, enhancing productivity while improving workplace safety. In healthcare, surgical robots enable procedures with unprecedented precision, reducing recovery times and improving patient outcomes. Exploration robots have ventured where humans cannot yet go, from the depths of our oceans to the surface of Mars, sending back data that expands our understanding of the universe.
//       Sustainability has emerged as a critical focus of innovation in recent decades. The urgent challenge of climate change has spurred remarkable advances in renewable energy technologies. Solar panel efficiency has improved dramatically while costs have plummeted. Wind turbines now generate clean energy at scales once thought impossible. Electric vehicles are becoming increasingly accessible, promising to reduce our dependence on fossil fuels. Battery technology continues to advance, addressing the intermittency challenges that have historically limited renewable energy adoption.
//       Looking toward the future, several emerging technologies hold extraordinary promise. Quantum computing, though still in its early stages, could revolutionize fields ranging from cryptography to drug discovery by solving problems that would take classical computers millennia to process. Biotechnology advances, including CRISPR gene editing, offer the potential to treat genetic disorders and combat diseases that have plagued humanity for generations. Advances in materials science are producing substances with remarkable properties, from self-healing materials to superconductors that could transform energy transmission.
//       Yet for all our technological achievements, we must remember that innovation is ultimately about people. The most sophisticated technology is meaningless if it does not serve human needs and values. As we continue to push the boundaries of what is possible, we must remain mindful of the ethical dimensions of our work. Questions of privacy, equity, and accessibility must be addressed thoughtfully. The benefits of innovation should be distributed broadly rather than concentrated among the few.
//       The journey of innovation is inherently collaborative. No significant breakthrough occurs in isolation. Each advancement builds upon the collective knowledge accumulated by countless individuals across generations and continents. Open source communities exemplify this spirit of collaboration, with developers around the world contributing their expertise to projects that benefit everyone. This model of shared progress embodies the best of human cooperation and suggests that our greatest achievements lie not in competition but in our capacity to work together toward common goals.
//       As we stand at this moment in history, we find ourselves with tools and capabilities that our ancestors could scarcely have imagined. The challenges we face are equally unprecedented, from climate change to global health crises to the social disruptions wrought by rapid technological change. Meeting these challenges will require not only technical expertise but also wisdom, empathy, and a commitment to the common good. The same innovative spirit that brought us this far will be essential as we navigate the complexities of the future.
//       Each of us, in our own way, participates in this ongoing story of human progress. Whether we are scientists conducting research, entrepreneurs building new ventures, educators sharing knowledge, or simply individuals striving to make a positive difference in our communities, we contribute to the collective enterprise of building a better world. The tools may change, but the fundamental aspiration remains the same: to understand, to create, and to improve the human condition.
//       This is the true measure of innovation. Not the speed of our processors or the sophistication of our algorithms, but our capacity to use our knowledge and creativity in service of something larger than ourselves. It is a journey without a final destination, an endless frontier of possibility waiting to be explored. And as we continue on this path, we carry forward the legacy of all those who came before us, while laying the groundwork for those who will follow.
//             """;
//     var file = new FileSystemResource(filePah);
//     // var prompt = new TextToSpeechPrompt(content);
//     // var response = model.call(prompt);
//     // file.getOutputStream().write(response.getResult().getOutput());
//     //简化调用
//     file.getOutputStream().write(model.call(content));
//     //流式调用
//     // var outputstream = file.getOutputStream();
//     // var stream = model.stream(content);
//     // stream.subscribe(
//     //   bytes -> {
//     //     try {
//     //       outputstream.write(bytes);
//     //       //   System.out.println(bytes.length);
//     //     } catch (IOException e) {
//     //       // TODO Auto-generated catch block
//     //       e.printStackTrace();
//     //     }
//     //   },
//     //   error -> {
//     //     System.out.println(error);
//     //   },
//     //   () -> {
//     //     try {
//     //       outputstream.close();
//     //     } catch (IOException e) {
//     //       // TODO Auto-generated catch block
//     //       e.printStackTrace();
//     //     }
//     //   }
//     // );
//   }
// }
