;; The contents of this file are subject to the LGPL License, Version 3.0.
;; 
;; Copyright (C) 2013, Phillip Lord, Newcastle University
;; 
;; This program is free software: you can redistribute it and/or modify it
;; under the terms of the GNU Lesser General Public License as published by
;; the Free Software Foundation, either version 3 of the License, or (at your
;; option) any later version.
;; 
;; This program is distributed in the hope that it will be useful, but WITHOUT
;; ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
;; FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License
;; for more details.
;; 
;; You should have received a copy of the GNU Lesser General Public License
;; along with this program. If not, see http://www.gnu.org/licenses/.

(ns tawny.pitfalls.only-test
  (:use [clojure.test])
  (:require [tawny owl reasoner]
            [tawny.pitfalls.only]))


(defn ontology-reasoner-fixture [tests]
  ;; this should kill the reasoner factory and all reasoners which is the
  ;; safest, but slowest way to start.
  (tawny.reasoner/reasoner-factory :hermit)
  
  ;; inject the pizzaontology into the current namespace, which saves the
  ;; hassle of using with ontology every where. set this up each time in case
  ;; pizzaontology has been re-evaled
  (tawny.owl/ontology-to-namespace tawny.pitfalls.only/only-range)
  (binding [tawny.reasoner/*reasoner-progress-monitor*
            tawny.reasoner/reasoner-progress-monitor-silent]
    (tests)))

(use-fixtures :once ontology-reasoner-fixture)


(deftest equivs
  (is 
   (tawny.reasoner/iequivalent-class? 
    tawny.pitfalls.only/C (tawny.owl/owlthing)))
  (is 
   (not 
    (tawny.reasoner/iequivalent-class? 
     tawny.pitfalls.only/E (tawny.owl/owlthing)))))
