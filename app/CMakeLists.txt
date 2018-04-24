# Sets the minimum version of CMake required to build the native
# library. You should either keep the default value or only pass a
# value of 3.4.0 or lower.

cmake_minimum_required(VERSION 3.4.1)

# Creates and names a library, sets it as either STATIC
# or SHARED, and provides the relative paths to its source code.
# You can define multiple libraries, and CMake builds it for you.
# Gradle automatically packages shared libraries with your APK.

add_library( # Sets the name of the library.
             jni_latinime

             # Sets the library as a shared library.
             SHARED

             # Provides a relative path to your source file(s).
             # Associated headers in the same location as their source
             # file are automatically included.
             src/main/cpp/dicttoolkit/dict_toolkit_main.cpp;
             src/main/cpp/dicttoolkit/src/command_executors/diff_executor.cpp;
             src/main/cpp/dicttoolkit/src/command_executors/header_executor.cpp;
             src/main/cpp/dicttoolkit/src/command_executors/help_executor.cpp;
             src/main/cpp/dicttoolkit/src/command_executors/info_executor.cpp;
             src/main/cpp/dicttoolkit/src/command_executors/makedict_executor.cpp;
             src/main/cpp/dicttoolkit/src/offdevice_intermediate_dict/offdevice_intermediate_dict.cpp;
             src/main/cpp/dicttoolkit/src/utils/arguments_parser.cpp;
             src/main/cpp/dicttoolkit/src/utils/command_utils.cpp;
             src/main/cpp/dicttoolkit/src/utils/utf8_utils.cpp;
             src/main/cpp/jni/typeofmood_ime_keyboard_ProximityInfo.cpp;
             src/main/cpp/jni/typeofmood_ime_latin_BinaryDictionary.cpp;
             src/main/cpp/jni/typeofmood_ime_latin_BinaryDictionaryUtils.cpp;
             src/main/cpp/jni/typeofmood_ime_latin_DicTraverseSession.cpp;
             src/main/cpp/jni/jni_common.cpp;
             src/main/cpp/jni/src/dictionary/header/header_policy.cpp;
             src/main/cpp/jni/src/dictionary/header/header_read_write_utils.cpp;
             src/main/cpp/jni/src/dictionary/property/ngram_context.cpp;
             src/main/cpp/jni/src/dictionary/structure/backward/v402/bigram/ver4_bigram_list_policy.cpp;
             src/main/cpp/jni/src/dictionary/structure/backward/v402/content/bigram_dict_content.cpp;
             src/main/cpp/jni/src/dictionary/structure/backward/v402/content/probability_dict_content.cpp;
             src/main/cpp/jni/src/dictionary/structure/backward/v402/content/shortcut_dict_content.cpp;
             src/main/cpp/jni/src/dictionary/structure/backward/v402/content/sparse_table_dict_content.cpp;
             src/main/cpp/jni/src/dictionary/structure/backward/v402/content/terminal_position_lookup_table.cpp;
             src/main/cpp/jni/src/dictionary/structure/backward/v402/ver4_dict_buffers.cpp;
             src/main/cpp/jni/src/dictionary/structure/backward/v402/ver4_dict_constants.cpp;
             src/main/cpp/jni/src/dictionary/structure/backward/v402/ver4_patricia_trie_node_reader.cpp;
             src/main/cpp/jni/src/dictionary/structure/backward/v402/ver4_patricia_trie_node_writer.cpp;
             src/main/cpp/jni/src/dictionary/structure/backward/v402/ver4_patricia_trie_policy.cpp;
             src/main/cpp/jni/src/dictionary/structure/backward/v402/ver4_patricia_trie_reading_utils.cpp;
             src/main/cpp/jni/src/dictionary/structure/backward/v402/ver4_patricia_trie_writing_helper.cpp;
             src/main/cpp/jni/src/dictionary/structure/backward/v402/ver4_pt_node_array_reader.cpp;
             src/main/cpp/jni/src/dictionary/structure/dictionary_structure_with_buffer_policy_factory.cpp;
             src/main/cpp/jni/src/dictionary/structure/pt_common/bigram/bigram_list_read_write_utils.cpp;
             src/main/cpp/jni/src/dictionary/structure/pt_common/dynamic_pt_gc_event_listeners.cpp;
             src/main/cpp/jni/src/dictionary/structure/pt_common/dynamic_pt_reading_helper.cpp;
             src/main/cpp/jni/src/dictionary/structure/pt_common/dynamic_pt_reading_utils.cpp;
             src/main/cpp/jni/src/dictionary/structure/pt_common/dynamic_pt_updating_helper.cpp;
             src/main/cpp/jni/src/dictionary/structure/pt_common/dynamic_pt_writing_utils.cpp;
             src/main/cpp/jni/src/dictionary/structure/pt_common/patricia_trie_reading_utils.cpp;
             src/main/cpp/jni/src/dictionary/structure/pt_common/shortcut/shortcut_list_reading_utils.cpp;
             src/main/cpp/jni/src/dictionary/structure/v2/patricia_trie_policy.cpp;
             src/main/cpp/jni/src/dictionary/structure/v2/ver2_patricia_trie_node_reader.cpp;
             src/main/cpp/jni/src/dictionary/structure/v2/ver2_pt_node_array_reader.cpp;
             src/main/cpp/jni/src/dictionary/structure/v4/content/dynamic_language_model_probability_utils.cpp;
             src/main/cpp/jni/src/dictionary/structure/v4/content/language_model_dict_content.cpp;
             src/main/cpp/jni/src/dictionary/structure/v4/content/language_model_dict_content_global_counters.cpp;
             src/main/cpp/jni/src/dictionary/structure/v4/content/shortcut_dict_content.cpp;
             src/main/cpp/jni/src/dictionary/structure/v4/content/sparse_table_dict_content.cpp;
             src/main/cpp/jni/src/dictionary/structure/v4/content/terminal_position_lookup_table.cpp;
             src/main/cpp/jni/src/dictionary/structure/v4/ver4_dict_buffers.cpp;
             src/main/cpp/jni/src/dictionary/structure/v4/ver4_dict_constants.cpp;
             src/main/cpp/jni/src/dictionary/structure/v4/ver4_patricia_trie_node_reader.cpp;
             src/main/cpp/jni/src/dictionary/structure/v4/ver4_patricia_trie_node_writer.cpp;
             src/main/cpp/jni/src/dictionary/structure/v4/ver4_patricia_trie_policy.cpp;
             src/main/cpp/jni/src/dictionary/structure/v4/ver4_patricia_trie_reading_utils.cpp;
             src/main/cpp/jni/src/dictionary/structure/v4/ver4_patricia_trie_writing_helper.cpp;
             src/main/cpp/jni/src/dictionary/structure/v4/ver4_pt_node_array_reader.cpp;
             src/main/cpp/jni/src/dictionary/utils/buffer_with_extendable_buffer.cpp;
             src/main/cpp/jni/src/dictionary/utils/byte_array_utils.cpp;
             src/main/cpp/jni/src/dictionary/utils/dict_file_writing_utils.cpp;
             src/main/cpp/jni/src/dictionary/utils/file_utils.cpp;
             src/main/cpp/jni/src/dictionary/utils/forgetting_curve_utils.cpp;
             src/main/cpp/jni/src/dictionary/utils/format_utils.cpp;
             src/main/cpp/jni/src/dictionary/utils/mmapped_buffer.cpp;
             src/main/cpp/jni/src/dictionary/utils/multi_bigram_map.cpp;
             src/main/cpp/jni/src/dictionary/utils/probability_utils.cpp;
             src/main/cpp/jni/src/dictionary/utils/sparse_table.cpp;
             src/main/cpp/jni/src/dictionary/utils/trie_map.cpp;
             src/main/cpp/jni/src/suggest/core/dicnode/dic_node.cpp;
             src/main/cpp/jni/src/suggest/core/dicnode/dic_node_utils.cpp;
             src/main/cpp/jni/src/suggest/core/dicnode/dic_nodes_cache.cpp;
             src/main/cpp/jni/src/suggest/core/dictionary/dictionary.cpp;
             src/main/cpp/jni/src/suggest/core/dictionary/dictionary_utils.cpp;
             src/main/cpp/jni/src/suggest/core/dictionary/digraph_utils.cpp;
             src/main/cpp/jni/src/suggest/core/dictionary/error_type_utils.cpp;
             src/main/cpp/jni/src/suggest/core/layout/additional_proximity_chars.cpp;
             src/main/cpp/jni/src/suggest/core/layout/proximity_info.cpp;
             src/main/cpp/jni/src/suggest/core/layout/proximity_info_params.cpp;
             src/main/cpp/jni/src/suggest/core/layout/proximity_info_state.cpp;
             src/main/cpp/jni/src/suggest/core/layout/proximity_info_state_utils.cpp;
             src/main/cpp/jni/src/suggest/core/policy/weighting.cpp;
             src/main/cpp/jni/src/suggest/core/result/suggestion_results.cpp;
             src/main/cpp/jni/src/suggest/core/result/suggestions_output_utils.cpp;
             src/main/cpp/jni/src/suggest/core/session/dic_traverse_session.cpp;
             src/main/cpp/jni/src/suggest/core/suggest.cpp;
             src/main/cpp/jni/src/suggest/policyimpl/gesture/gesture_suggest_policy_factory.cpp;
             src/main/cpp/jni/src/suggest/policyimpl/typing/scoring_params.cpp;
             src/main/cpp/jni/src/suggest/policyimpl/typing/typing_scoring.cpp;
             src/main/cpp/jni/src/suggest/policyimpl/typing/typing_suggest_policy.cpp;
             src/main/cpp/jni/src/suggest/policyimpl/typing/typing_traversal.cpp;
             src/main/cpp/jni/src/suggest/policyimpl/typing/typing_weighting.cpp;
             src/main/cpp/jni/src/utils/autocorrection_threshold_utils.cpp;
             src/main/cpp/jni/src/utils/char_utils.cpp;
             src/main/cpp/jni/src/utils/jni_data_utils.cpp;
             src/main/cpp/jni/src/utils/log_utils.cpp;
             src/main/cpp/jni/src/utils/time_keeper.cpp)

# Specifies a path to native header files.
include_directories(src/main/cpp/dicttoolkit/src/command_executors/;
                    src/main/cpp/dicttoolkit/src/;
                    src/main/cpp/dicttoolkit/src/offdevice_intermediate_dict/;
                    src/main/cpp/dicttoolkit/src/utils/;
                    src/main/cpp/jni/;
                    src/main/cpp/jni/src/;
                    src/main/cpp/jni/src/dictionary/header/;
                    src/main/cpp/jni/src/dictionary/interface/;
                    src/main/cpp/jni/src/dictionary/property/;
                    src/main/cpp/jni/src/dictionary/structure/backward/v402/bigram/;
                    src/main/cpp/jni/src/dictionary/structure/backward/v402/content/;
                    src/main/cpp/jni/src/dictionary/structure/backward/v402/shortcut/;
                    src/main/cpp/jni/src/dictionary/structure/backward/v402/;
                    src/main/cpp/jni/src/dictionary/structure/;
                    src/main/cpp/jni/src/dictionary/structure/pt_common/bigram/;
                    src/main/cpp/jni/src/dictionary/structure/pt_common/;
                    src/main/cpp/jni/src/dictionary/structure/pt_common/shortcut/;
                    src/main/cpp/jni/src/dictionary/structure/v2/bigram/;
                    src/main/cpp/jni/src/dictionary/structure/v2/;
                    src/main/cpp/jni/src/dictionary/structure/v2/shortcut/;
                    src/main/cpp/jni/src/dictionary/structure/v4/content/;
                    src/main/cpp/jni/src/dictionary/structure/v4/shortcut/;
                    src/main/cpp/jni/src/dictionary/structure/v4/;
                    src/main/cpp/jni/src/dictionary/utils/;
                    src/main/cpp/jni/src/suggest/core/dicnode/;
                    src/main/cpp/jni/src/suggest/core/dicnode/internal/;
                    src/main/cpp/jni/src/suggest/core/dictionary/;
                    src/main/cpp/jni/src/suggest/core/layout/;
                    src/main/cpp/jni/src/suggest/core/policy/;
                    src/main/cpp/jni/src/suggest/core/result/;
                    src/main/cpp/jni/src/suggest/core/;
                    src/main/cpp/jni/src/suggest/policyimpl/gesture/;
                    src/main/cpp/jni/src/suggest/policyimpl/typing/;
                    src/main/cpp/jni/src/suggest/policyimpl/utils/;
                    src/main/cpp/jni/src/utils/)

# Searches for a specified prebuilt library and stores the path as a
# variable. Because system libraries are included in the search path by
# default, you only need to specify the name of the public NDK library
# you want to add. CMake verifies that the library exists before
# completing its build.

find_library( # Sets the name of the path variable.
              log-lib

              # Specifies the name of the NDK library that
              # you want CMake to locate.
              log )

# Specifies libraries CMake should link to your target library. You
# can link multiple libraries, such as libraries you define in the
# build script, prebuilt third-party libraries, or system libraries.

target_link_libraries( # Specifies the target library.
                       jni_latinime

                       # Links the target library to the log library
                       # included in the NDK.
                       ${log-lib} )
